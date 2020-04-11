package student;

import student.constraints.BlockLenCorrespondsToMaxBlockLen;
import student.constraints.SequenceColorsCountIsCorrect;
import student.constraints.SequenceDoesNotOverlap;
import student.constraints.SequenceHasValidColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Constraint-satisfaction problem solving - nonograms.
 *
 * Main techniques: backtracking + backjumping
 * Constraint-propagation techniques: AC3 + Forward checking.
 * Variables are selected with the most constrained heuristics.
 * Values are selected with the least constraining heuristics.
 *
 * Variables:
 * int[i][j] varRow - i: row index, j: row block index. Value is the start position of the row block
 * int[i][j] varCol - i: column index, j: column block index. Value is the start position of the column block
 * String[i][j] grid - i: row index, j: column index. Value is the color of the cell(i,j). This is just auxiliary variable.
 *
 * Domains:
 *
 * Block[i][j] domainRow - i: row index, j: row block index. Value contains row block values as: color, block length (rowBlockLength)
 *                         and all possible start positions of the block within the row.
 * Block[i][j] domainCol - i: column index, j: column block index. Value contains column block values as: color, block length (colBlockLength)
 *                         and all possible start positions of the block within the column.
 *
 *
 * Constraints:
 *
 * 1. sequenceDoesNotOverlap:
 *     a) for all i in rows:
 *            for all defined row blocks k, k + 1:
 *                varRow[i][k] + rowBlockLength[i][k] < varRow[i][k + 1]    (if colors of k,k+1 blocks equal, then <=)
 *
 *     b) for all i in columns:
 *            for all defined col blocks k, k + 1:
 *                varCol[i][k] + colBlockLength[i][k] < varCol[i][k + 1]    (if colors of k,k+1 blocks equal, then <=)
 *
 * 2. sequenceContainsOnlyValidColors
 *    a) for each row i:
 *
 *      Constraint A
 *      =============
 *           let firstBlockStart = first row block start position ( = varRow[i][0])
 *           let columns = all grid cells at row i
 *
 *           Then if firstBlockStart is defined (!= -1), then:
 *               columns from 0 to the firstBlockStart has to be empty
 *
 *      Constraint B
 *      ============
 *          for all *defined* row blocks k, k + 1:
 *            let spaces (between blocks k and (k+1)) = start of block (k+1) - (start of block k + it's length),
 *                or more formally:
 *            let spaces                              = varRow[i][k+1]       - (varRow[i][k]     + rowBlockLength[i][k])
 *            All the spaces must be empty (contain no color).
 *
 *      Constraint C
 *      ============
 *          let lastBlockIndex = varRow[i].length - 1
 *          let lastBlockStart = varRow[i][lastBlockIndex]
 *
 *          Then if lastBlockStart is defined ( != -1):
 *              columns from (where the last block ends) to the end of the line must be empty.
 *
 *          Note: last block ends at (lastBlockStart + rowBlockLength[i][lastBlockIndex])
 *
 *    b) for all column i:
 *
 *      Constraint A
 *      =============
 *           let firstBlockStart = first column block start position ( = varCol[i][0])
 *           let rows = all grid cells at column i
 *
 *           Then if firstBlockStart is defined (!= -1), then:
 *               rows from 0 to the firstBlockStart has to be empty
 *
 *      Constraint B
 *      ============
 *          for all *defined* column blocks k, k + 1:
 *            let spaces (between blocks k and (k+1)) = start of block (k+1) - (start of block k + it's length),
 *                or more formally:
 *            let spaces                              = varCol[i][k+1]       - (varCol[i][k]     + colBlockLength[i][k])
 *            All the spaces must be empty (contain no color).
 *
 *      Constraint C
 *      ============
 *          let lastBlockIndex = varCol[i].length - 1
 *          let lastBlockStart = varCol[i][lastBlockIndex]
 *
 *          Then if lastBlockStart is defined ( != -1):
 *              rows from (where the last block ends) to the bottom of grid in the same column must be empty.
 *
 *          Note: last block ends at (lastBlockStart + colBlockLength[i][lastBlockIndex])
 *
 *    Notes: If between two blocks exist undefined blocks, the space between them is not checked. Also, if the first
 *          block is not defined, the initial space is not checked; and if the last block is not defined - space after
 *          the block is not checked.
 *
 *
 *
 */
public class CSPMain {

    private static void printGrid(String[][] grid) {
        for (String[] aGrid : grid) {
            for (String anAGrid : aGrid) {
                if (anAGrid.isEmpty()) {
                    System.out.print("_");
                } else {
                    System.out.print(anAGrid);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    static void fillBlockOnGrid(String[][] grid, Selection sel, int startPos) {
        Block block = sel.block();
        GridDimension dim = sel.toGridDimension(startPos);

        // draw the block
        for (int k = 0; k < block.length; k++) {
            dim.set(grid, block.color);
            dim.advance();
        }

     //   printGrid(grid);
    }

    private static String[][] cloneGrid(String[][] grid) {
        String[][] clone = new String[grid.length][grid[0].length];
        for (int i = 0; i < clone.length; i++) {
            System.arraycopy(grid[i], 0,clone[i], 0, grid[i].length);
        }
        return clone;
    }

    private static boolean removeValues(String[][] grid, Selection x, Selection y, Domains domains) {
        boolean removed = false;

        Assignment assignment = y.assignment;

        List<Integer> xDomain = domains.domain(x);
        List<Integer> yDomain = domains.domain(y);
        List<Integer> yDomainBackup = new ArrayList<>(yDomain);
        for (int yValue : yDomainBackup) {
            String[][] gridClone = cloneGrid(grid);
          //  System.out.println("Y = " + y + " => " + yValue + " xDomain = " + xDomain);

            assignment.assign(y, gridClone, yValue);

            boolean found = false;
            for (int xValue : xDomain) {
//                System.out.println("X = " + x + " => " + xValue + "( X[0][0][0]=" + assignment.variables[0][0][0] + ")"
//                        + " (Y[1][0][0] = " + assignment.variables[1][0][0] + ")"
//                );

                String[][] gridCloneSecond = cloneGrid(gridClone);
                assignment.assign(x, gridCloneSecond, xValue);

                boolean xValid = assignment.isConsistent(x, gridCloneSecond)
                        && assignment.isConsistent(y, gridCloneSecond);

//                if (!xValid) {
//                    printGrid(gridCloneSecond);
//                }

                assignment.unassign(x);
                if (xValid) {
                    found = true;
                 //   System.out.println("  -> VALID");
                    break;
                }
            }
            if (!found) {
                yDomain.remove((Integer) yValue);
                removed = true;
            }
            assignment.unassign(y);
//            if (yDomain.isEmpty()) {
//                break;
//            }
        }

        return removed;
    }


    private static boolean ac3(String[][] grid, Assignment assignment, Domains domains) {
        Queue<Selection> queue = new LinkedList<>();

        // put all uninitialized variables into queue
        queue.addAll(assignment.freeVariables());
        while (!queue.isEmpty()) {
            Selection x = queue.poll();

            for (Selection y : assignment.relatedVariables(x, domains)) {
                if (removeValues(grid, x, y, domains)) {
                    if (domains.domain(y).isEmpty()) {
                        return false;
                    }
                    if (!queue.contains(y)) {
                        queue.add(y);
                    }
                }
            }
        }
        return true;
    }

    // returns -1 if some domain is empty
    // returns number of reduced domains otherwise
    private static int forwardChecking(Selection realSel, String[][] grid, Assignment assignment, Domains domains) {
        int reducedCount = 0;

        for (Selection sel : assignment.relatedVariables(realSel, domains)) {  // .freeVariables()) {
            List<Integer> domain = domains.domain(sel);
            int originalDomainSize = domain.size();

            for (int value : new ArrayList<>(domain)) {
                String[][] gridClone = cloneGrid(grid);
                assignment.assign(sel, gridClone, value);

                boolean isValid = assignment.isConsistent(sel, gridClone);
                Block block = sel.block();
                for (int k = value; isValid && k < value + block.length; k++) {
                    isValid = assignment.isConsistent(sel.transposed(k, 0), gridClone);
                }
                if (!isValid) {
                    domain.remove((Integer)value);
                }
                assignment.unassign(sel);
            }

            int tmpCount = originalDomainSize - domain.size();
            reducedCount += tmpCount;

            if (domain.isEmpty()) {
                // if domain is empty, do not continue
                System.out.println("FWD!!!");
                return -1;
            }
        }
        return reducedCount;
    }
//
//    static long nestLevel = 0;
//    static long iterationsCount = 0;
//    static long start = 0;

    private static boolean csp(String[][] grid, Assignment assignment, Domains domains) {
//        if (start == 0) {
//            start = System.currentTimeMillis();
//        }
//
//        System.out.println(String.format("%06d (nest %06d) - %s (%d seconds)", iterationsCount++, nestLevel++,
//                assignment.stats(),
//                TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start)));

        if (assignment.isComplete()) {
            printGrid(grid);
           // nestLevel--;
            return true;
        }

        Domains domainsClone = domains.backup();
        if (!ac3(grid, assignment, domainsClone)) {
          //  nestLevel--;
            return false;
        }

        Selection sel = assignment.select(domainsClone);

        class LeastConstrainingValue {
            private final int value;
            private final Domains domains;

            private LeastConstrainingValue(int value, Domains domains) {
                this.value = value;
                this.domains = domains;
            }
        }

        NavigableMap<Integer, List<LeastConstrainingValue>> leastConstValues = new TreeMap<>();
        for (int value : domainsClone.domain(sel)) {
            String[][] gridBackup = cloneGrid(grid);
            assignment.assign(sel, grid, value);

            Domains domainsCloneBackup = domainsClone.backup();
            int reduced = forwardChecking(sel, grid, assignment, domainsCloneBackup);
            if (reduced >= 0) {
                if (!leastConstValues.containsKey(reduced)) {
                    leastConstValues.put(reduced, new ArrayList<>());
                }
                leastConstValues.get(reduced).add(new LeastConstrainingValue(value, domainsCloneBackup));
                continue;
            }
            assignment.unassign(sel);
            grid = gridBackup;
        }

        boolean foundAtLeastOne = false;
        for (Map.Entry<Integer, List<LeastConstrainingValue>> lcValueList : leastConstValues.entrySet()) {
            for (LeastConstrainingValue lcValue : lcValueList.getValue()) {
                String[][] gridBackup = cloneGrid(grid);
                assignment.assign(sel, grid, lcValue.value);

                foundAtLeastOne = foundAtLeastOne | csp(grid, assignment, lcValue.domains);

                assignment.unassign(sel);
                grid = gridBackup;
            }
        }

     //   nestLevel--;
        return foundAtLeastOne;
    }


    private static void createDomain(int dimension, int count, int transposedCount, Domains domains, Block[][][] blocks,
                                     Scanner sc) {
        domains.domain[dimension] = new List[count][];
        blocks[dimension] = new Block[count][];

        for (int index = 0; index < count; index++) {
            List<String> colors = new ArrayList<>();
            List<Integer> leftMost = new ArrayList<>();
            List<Integer> rightMost = new ArrayList<>();
            List<Integer> lengths = new ArrayList<>();

            String[] blockConstraint = sc.nextLine().split(",");

            int sum = 0;
            String previousColor = "";
            for (int j = 0; j < blockConstraint.length - 1; j += 2) {
                if (!previousColor.isEmpty() || previousColor.equals(blockConstraint[j])) {
                    sum++;
                }
                leftMost.add(sum);
                colors.add(blockConstraint[j]);

                int blockLength = Integer.parseInt(blockConstraint[j + 1]);
                lengths.add(blockLength);

                sum += blockLength;
            }
            sum = transposedCount;
            for (int j = blockConstraint.length - 2; j >= 0; j -= 2) {
                sum -= Integer.parseInt(blockConstraint[j + 1]);
                rightMost.add(0, sum);
            }

            domains.domain[dimension][index] = new List[colors.size()];
            blocks[dimension][index] = new Block[colors.size()];
            for (int blockIndex = 0; blockIndex < colors.size(); blockIndex++) {
                List<Integer> domain = new ArrayList<>();
                domains.domain[dimension][index][blockIndex] = domain;

                String color = colors.get(blockIndex);
                for (int k = leftMost.get(blockIndex); k <= rightMost.get(blockIndex); k++) {
                    domain.add(k);
                }

                blocks[dimension][index][blockIndex] = new Block(lengths.get(blockIndex), color);
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] rc = sc.nextLine().split(",");
        int rows = Integer.parseInt(rc[0]);
        int cols = Integer.parseInt(rc[1]);

        Domains domains = new Domains(new List[2][][]);
        Block[][][] blocks = new Block[2][][];

        createDomain(0, rows, cols, domains, blocks, sc);
        createDomain(1, cols, rows, domains, blocks, sc);

        Assignment assignment = new Assignment(blocks, Arrays.asList(
                new BlockLenCorrespondsToMaxBlockLen(),
                new SequenceColorsCountIsCorrect(),
                new SequenceDoesNotOverlap(),
                new SequenceHasValidColors()
        ));

        String[][] grid = new String[rows][cols];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = "";
            }
        }

        long startTime = System.nanoTime();
        if (!csp(grid, assignment, domains)) {
            System.out.println("null");
        }
        long endTime = System.nanoTime();

        System.out.println("TIME : " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
    }
}