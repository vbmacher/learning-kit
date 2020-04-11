package student;

import student.constraints.SequenceDoesNotOverlap;
import student.constraints.SequenceHasValidColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
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
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].isEmpty()) {
                    System.out.print("_");
                } else {
                    System.out.print(grid[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();
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
            if (!assignment.assign(y, gridClone, yValue)) {
                yDomain.remove((Integer) yValue);
                removed = true;
                continue;
            }

            boolean yValueIsValid = assignment.isConsistent(y, gridClone, domains);
            if (!yValueIsValid) {
                assignment.unassignLast();
                yDomain.remove((Integer) yValue);
                removed = true;
                continue;
            }

            boolean found = false;
            for (int xValue : xDomain) {
                String[][] gridCloneSecond = cloneGrid(gridClone);
                if (!assignment.assign(x, gridCloneSecond, xValue)) {
                    continue;
                }

                boolean xValid = assignment.isConsistent(x, gridCloneSecond, domains)
                        && assignment.isConsistent(y, gridCloneSecond, domains);

                assignment.unassignLast();
                if (xValid) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                yDomain.remove((Integer) yValue);
                removed = true;
            }
            assignment.unassignLast();
            if (yDomain.isEmpty()) {
                break;
            }
        }

        return removed;
    }


    private static boolean ac3(String[][] grid, Assignment assignment, Domains domains) {
        Queue<Selection> queue = new LinkedList<>();

        // put all uninitialized variables into queue
        queue.addAll(assignment.freeVariables());
        while (!queue.isEmpty()) {
            Selection x = queue.poll();

            for (Selection y : assignment.relatedVariables(x, domains, true)) {
                if (removeValues(grid, x, y, domains)) {
                    if (domains.domain(y).isEmpty()) {
                        return false;
                    }
                    queue.add(y);
                }
            }
        }
        return true;
    }

    // returns -1 if some domain is empty
    // returns number of reduced domains otherwise
    private static int forwardChecking(String[][] grid, Assignment assignment, Domains domains) {
        int reducedCount = 0;

        for (Selection sel : assignment.freeVariables()) {
            List<Integer> domain = domains.domain(sel);
            int originalDomainSize = domain.size();

            for (int value : new ArrayList<>(domain)) {
                String[][] gridClone = cloneGrid(grid);
                if (!assignment.assign(sel, gridClone, value)) {
                    continue;
                }

                if (!assignment.isConsistent(sel, gridClone, domains)) {
                    domain.remove((Integer)value);
                }
                assignment.unassignLast();
            }

            int tmpCount = originalDomainSize - domain.size();
            reducedCount += tmpCount;

            if (domain.isEmpty()) {
                // if domain is empty, do not continue
                return -1;
            }
        }
        return reducedCount;
    }

    static class LeastConstrainingValue {
        private final int value;
        private final Domains domains;

        private LeastConstrainingValue(int value, Domains domains) {
            this.value = value;
            this.domains = domains;
        }
    }

    private static List<LeastConstrainingValue> findLeastConstrainingValues(Domains domains, Selection sel,
                                                                            Assignment assignment, String[][] grid) {
        NavigableMap<Integer, List<LeastConstrainingValue>> leastConstValues = new TreeMap<>();
        for (int value : domains.domain(sel)) {
            String[][] gridBackup = cloneGrid(grid);
            if (!assignment.assign(sel, grid, value)) {
                continue;
            }

            Domains domainsCloneBackup = domains.backup();
            int reduced = forwardChecking(grid, assignment, domainsCloneBackup);
            if (reduced == -1) {
                grid = gridBackup;
                assignment.unassignLast();
                continue;
            }
            if (!leastConstValues.containsKey(reduced)) {
                leastConstValues.put(reduced, new ArrayList<>());
            }
            leastConstValues.get(reduced).add(new LeastConstrainingValue(value, domainsCloneBackup));
            assignment.unassignLast();
            grid = gridBackup;
        }

        List<LeastConstrainingValue> result = new ArrayList<>();
        for (Map.Entry<Integer, List<LeastConstrainingValue>> entry : leastConstValues.entrySet()) {
            result.addAll(entry.getValue());
        }
        return result;
    }

    private static Optional<Selection> findFirstInconsistent(Assignment assignment, String[][] grid, Selection sel,
                                                             int value, Domains domains) {
        if (!assignment.assign(sel, grid, value)) {
            return Optional.of(assignment.lastInconsistency);
        }
        if (!assignment.isConsistent(sel, grid, domains)) {
            assignment.unassignLast();
            return Optional.ofNullable(assignment.lastInconsistency);
        }
        return Optional.empty();
    }

    private static Collection<Selection> generalisedBackjumping(Assignment assignment, String[][] grid, Selection sel,
                                                                Domains domains) {
        List<Selection> parentsToBlame = new ArrayList<>();

        System.out.println("Backjumping; " + sel);
        for (int value : domains.domain(sel)) { //findLeastConstrainingValues(domains, sel, assignment, grid)) {
            System.out.println("  Trying " + value);

            String[][] gridClone = cloneGrid(grid);
            Optional<Selection> firstInconsistent = findFirstInconsistent(assignment, gridClone, sel, value, domains);
            System.out.println("  Inconsistency: " + firstInconsistent);
            if (firstInconsistent.isPresent()) {
                System.out.println("    Blaming: " + firstInconsistent.get());
                parentsToBlame.add(firstInconsistent.get());
            } else {
                printGrid(gridClone);
                if (assignment.isComplete()) {
                    System.out.println("S.U.C.C.E.S.S.");
                    printGrid(gridClone);
                    assignment.unassignLast();
                    return Collections.emptyList();
                } else {
                    boolean everythingsFine = true;
                    for (Selection newSel : assignment.freeVariables()) {
                        Collection<Selection> recursive = generalisedBackjumping(
                                assignment, gridClone, newSel, domains
                        );
                        if (!recursive.isEmpty()) {
                            everythingsFine = false;

                            // find xi
                            boolean found = false;
                            for (Selection recSel : recursive) {
                                if (recSel.equals(sel)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                assignment.unassignLast();
                                return recursive;
                            }
                            parentsToBlame.addAll(recursive);
                        }
                    }
                    if (everythingsFine) {
                        assignment.unassignLast();
                        return Collections.emptyList();
                    }
                }
                assignment.unassignLast();
            }
        }
        return parentsToBlame;
    }


    static long time = -1;
    private static boolean csp(String[][] grid, Assignment assignment, Domains domains) {
        if (assignment.isComplete()) {
            printGrid(grid);
            return true;
        }

        Domains domainsClone = domains.backup();
        if (!ac3(grid, assignment, domainsClone)) {
            return false;
        }

        Selection sel = assignment.select(domainsClone);
        Collection<Selection> parentsToBlame = generalisedBackjumping(assignment, grid, sel, domainsClone);
        if (parentsToBlame.isEmpty()) {
            return true;
        } else {
            System.out.println("FAILED! backjumps=" + parentsToBlame);
        }




        boolean foundAtLeastOne = false;
//        for (LeastConstrainingValue lcValue : findLeastConstrainingValues(domainsClone, sel, assignment, grid)) {
//            String[][] gridBackup = cloneGrid(grid);
//            if (!assignment.assign(sel, grid, lcValue.value)) {
//                continue;
//            }
//
//            if (time == -1) {
//                time = System.currentTimeMillis();
//                printGrid(grid);
//            } else if (System.currentTimeMillis() - time > TimeUnit.MINUTES.toMillis(1)) {
//                printGrid(grid);
//                time = System.currentTimeMillis();
//            }
//
//            foundAtLeastOne = foundAtLeastOne | csp(grid, assignment, lcValue.domains);
//
//            assignment.unassignLast();
//            grid = gridBackup;
//        }

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
                new SequenceDoesNotOverlap(),
                new SequenceHasValidColors()
//                new SequenceColorsCountIsCorrect(),
  //              new BlockLenCorrespondsToMaxBlockLen()
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