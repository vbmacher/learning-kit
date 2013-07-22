package student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * Constraint-satisfaction problem solving - nonograms.
 *
 * Constraint-propagation techniques: AC3 + Forward checking.
 * Variables are selected with most constrained heuristics.
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

    static class Block {
        final List<Integer> starts;
        final int length;
        final String color;

        Block(String color, List<Integer> starts, int length) {
            this.color = Objects.requireNonNull(color);
            this.starts = Objects.requireNonNull(starts);
            this.length = length;
        }

        Block copy() {
            return new Block(color, new ArrayList<>(starts), length);
        }
    }

    static class Selection {
        private final int index;
        private final int blockIndex;

        private final Block block;
        private final boolean isRow;

        Selection(boolean isRow, int index, int blockIndex, Block block) {
            this.index = index;
            this.blockIndex = blockIndex;
            this.block = block;
            this.isRow = isRow;
        }
    }

    private static boolean isVarComplete(int[][] var) {
        for (int i = 0; i < var.length; i++) {
            for (int j = 0; j < var[i].length; j++) {
                if (var[i][j] == -1) {
                    return false;
                }
            }
        }
        return true;
    }

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

    private static NavigableMap<Integer, Selection> findFreeVars(boolean isRowVar, int[][] var, Block[][] domain) {
        NavigableMap<Integer, Selection> freeVars = new TreeMap<>(); // ordered by size of block domain(most-constrained heuristics)

        for (int index = 0; index < var.length; index++) {
            for (int blockIndex = 0; blockIndex < var[index].length; blockIndex++) {
                if (var[index][blockIndex] == -1) {
                    Block block = domain[index][blockIndex];
                    int count = block.starts.size();
                    freeVars.put(count, new Selection(isRowVar, index, blockIndex, block));
                }
            }
        }
        return freeVars;
    }

    private static Selection selectSingleVar(boolean isRowVar, int[][] var, Block[][] domain) {
        NavigableMap<Integer, Selection> freeVars = findFreeVars(isRowVar, var, domain);
        return freeVars.isEmpty() ? null : freeVars.firstEntry().getValue();
    }

    private static Selection selectVar(int[][] varRow, int[][] varCol, Block[][] domainRow, Block[][] domainCol) {
        Selection selRow = selectSingleVar(true, varRow, domainRow);
        Selection selCol = selectSingleVar(false, varCol, domainCol);

        Selection sel = selRow;
        if (selRow != null && selCol != null) {
            if (selCol.block.starts.size() < selRow.block.starts.size()) {
                sel = selCol;
            }
        } else {
            sel = selCol;
        }
        return sel;
    }

    private static boolean fillBlockOnGrid(boolean isRowIndex, String[][] grid, Block block, int startPos, int index) {
        String color = block.color;

        // verify if we can put selected block on the grid
        for (int k = 0; k < block.length; k++) {
            int row = isRowIndex ? index : (startPos + k);
            int col = isRowIndex ? (startPos + k) : index;

            if (!(grid[row][col].isEmpty() || grid[row][col].equals(color))) {
                // cell already has a different color
                return false;
            }
        }

        // draw the block
        for (int k = 0; k < block.length; k++) {
            int row = isRowIndex ? index : (startPos + k);
            int col = isRowIndex ? (startPos + k) : index;

            grid[row][col] = color;
        }
        return true;
    }

    private static String[][] cloneGrid(String[][] grid) {
        String[][] clone = new String[grid.length][grid[0].length];
        for (int i = 0; i < clone.length; i++) {
            System.arraycopy(grid[i], 0,clone[i], 0, grid[i].length);
        }
        return clone;
    }

    private static Block[][] cloneDomain(Block[][] domain) {
        Block[][] newDomain = new Block[domain.length][];
        for (int i = 0; i < domain.length; i++) {
            Block[] blocks = domain[i];
            newDomain[i] = new Block[blocks.length];

            for (int j = 0; j < blocks.length; j++) {
                newDomain[i][j] = blocks[j].copy();
            }
        }
        return newDomain;
    }

    private static boolean sequenceDoesNotOverlap(int[][] var, Block[][] domain, int index) {
        for (int k = 0; k < domain[index].length - 1; k++) {
            if (var[index][k] == -1 || var[index][k + 1] == -1) {
                // if block k and (k+1) are both undefined, overlapping for k-th block must not be checked
                continue;
            }

            Block blockK = domain[index][k];
            Block blockKplusOne = domain[index][k + 1];

            int endBlockK = var[index][k] + blockK.length;
            int startBlockKplusOne = var[index][k + 1];

            if (blockK.color.equals(blockKplusOne.color) && (endBlockK >= startBlockKplusOne)) {
                return false;
            } else if (endBlockK > startBlockKplusOne) {
                return false;
            }
        }
        return true;
    }


    private static boolean sequenceContainsOnlyValidColors(boolean indexIsRow, String[][] grid, int[][] var,
                                                           Block[][] domain, int index) {
        int blockStart = 0;
        int lastK = -1;

        for (int k = 0; k < var[index].length; k++) {
            if (var[index][k] != -1) {
                // check spaces / valid colors from start until the first block and between subsequent defined pair of blocks
                while (blockStart < var[index][k]) {
                    int row = indexIsRow ? index : blockStart;
                    int col = indexIsRow ? blockStart : index;

                    if (((k == 0) || (lastK == k - 1)) && !grid[row][col].isEmpty()) {
                        return false;
                    }
                    blockStart++;
                }
                blockStart += domain[index][k].length;
                lastK = k;
            }
        }
        int until = indexIsRow ? grid[index].length : grid.length;
        while (blockStart > 0 && (lastK == var[index].length - 1) && blockStart < until) {
            int row = indexIsRow ? index : blockStart;
            int col = indexIsRow ? blockStart : index;

            if (!grid[row][col].isEmpty()) {
                return false;
            }
            blockStart++;
        }
        return true;
    }

    private static Collection<Selection> findRelatedVars(boolean transposedIsRow, Block block,
                                                         int[][] transposedVar, Block[][] transposedDomain) {
        List<Selection> relatedVars = new ArrayList<>();

        int startPos = block.starts.get(0);
        int stopPos = block.starts.get(block.starts.size() - 1) + block.length;

        for (int index = startPos; index < stopPos; index++) {
            for (int blockIndex = 0; blockIndex < transposedVar[index].length; blockIndex++) {
                if (transposedVar[index][blockIndex] == -1) {
                    relatedVars.add(new Selection(
                            transposedIsRow, index, blockIndex, transposedDomain[index][blockIndex]
                    ));
                }
            }

        }
        return relatedVars;
    }

    private static boolean assign(Selection sel, int[][] var, String [][] grid, int value) {
        if (!fillBlockOnGrid(sel.isRow, grid, sel.block, value, sel.index)) {
            return false;
        }
        var[sel.index][sel.blockIndex] = value;
        return true;
    }

    private static void unassign(Selection sel, int[][] var) {
        var[sel.index][sel.blockIndex] = -1;
    }


    private static boolean removeValues(final String[][] grid, Selection x, Selection y,
                                        Block[][] xDomain, Block[][] yDomain, int[][] varX, int[][] varY) {
        boolean removed = false;
        Block yBlock = yDomain[y.index][y.blockIndex];
        Block xBlock = xDomain[x.index][x.blockIndex];

        List<Integer> yStarts = new ArrayList<>(yBlock.starts);
        for (int yValue : yStarts) {
            String[][] gridClone = cloneGrid(grid);
            if (!assign(y, varY, gridClone, yValue)) {
                yBlock.starts.remove((Object)yValue);
                removed = true;
                continue;
            }

            boolean yValueIsValid = sequenceDoesNotOverlap(varY, yDomain, y.index)
                    && sequenceContainsOnlyValidColors(y.isRow, gridClone, varY, yDomain, y.index);

            if (!yValueIsValid) {
                unassign(y, varY);
                yBlock.starts.remove((Object)yValue);
                removed = true;
                continue;
            }

            boolean found = false;
            for (int xValue : xBlock.starts) {
                String[][] gridCloneSecond = cloneGrid(gridClone);
                if (!assign(x, varX, gridCloneSecond, xValue)) {
                    continue;
                }

                boolean xValid = sequenceDoesNotOverlap(varX, xDomain, x.index)
                        && sequenceContainsOnlyValidColors(x.isRow, gridCloneSecond, varX, xDomain, x.index)
                        && sequenceContainsOnlyValidColors(y.isRow, gridCloneSecond, varY, yDomain, y.index);

                unassign(x, varX);
                if (xValid) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                yBlock.starts.remove((Object)yValue);
                removed = true;
            }
            unassign(y, varY);
        }

        return removed;
    }


    private static boolean ac3(String[][] grid, int[][] varRow, int[][] varCol, Block[][] domainRow, Block[][] domainCol) {
        Queue<Selection> queue = new LinkedList<>();

        // put all uninitialized variables into queue
        queue.addAll(findFreeVars(true, varRow, domainRow).values());
        queue.addAll(findFreeVars(false, varCol, domainCol).values());

        while (!queue.isEmpty()) {
            Selection sel = queue.poll();

            int[][] var = sel.isRow ? varRow : varCol;
            int[][] transposedVar = sel.isRow ? varCol : varRow;
            Block[][] domain = sel.isRow ? domainRow : domainCol;
            Block[][] transposedDomain = sel.isRow ? domainCol : domainRow;

            for (Selection relatedSel : findRelatedVars(!sel.isRow, sel.block, transposedVar, transposedDomain)) {
                if (removeValues(grid, sel, relatedSel, domain, transposedDomain, var, transposedVar)) {
                    if (transposedDomain[relatedSel.index][relatedSel.blockIndex].starts.isEmpty()) {
                        return false;
                    }
                    queue.add(relatedSel);
                }
            }
        }
        return true;
    }

    // returns -1 if some domain is empty
    // returns number of reduced domains otherwise
//    private static int forwardChecking(boolean checkRowVar, String[][] grid, int[][] varRow, int[][] varCol,
//                                           Block[][] domainRow, Block[][] domainCol) {
//
//        int[][] var = checkRowVar ? varRow : varCol;
//        int[][] transposedVar = checkRowVar ? varCol : varRow;
//        Block[][] domain = checkRowVar ? domainRow : domainCol;
//        Block[][] transposedDomain = checkRowVar ? domainCol : domainRow;
//
//        int reducedCount = 0;
//        for (Selection sel : findFreeVars(checkRowVar, var, domain).values().stream().flatMap(Collection::stream).collect(Collectors.toList())) {
//            List<Integer> validStarts = new ArrayList<>();
//            Block block = domain[sel.index][sel.blockIndex];
//
//            for (int value : block.starts) {
//                String[][] gridClone = cloneGrid(grid);
//                if (!assign(sel, var, gridClone, value)) {
//                    continue;
//                }
//
//                boolean isValid = sequenceDoesNotOverlap(var, domain, sel.index)
//                        && sequenceContainsOnlyValidColors(checkRowVar, gridClone, var, domain, sel.index);
//
//                for (int k = value; isValid && k < value + block.length; k++) {
//                    isValid = sequenceContainsOnlyValidColors(!checkRowVar, gridClone, transposedVar, transposedDomain, k);
//                }
//                if (isValid) {
//                    validStarts.add(value);
//                }
//
//                unassign(sel, var);
//            }
//
//            int tmpCount = block.starts.size() - validStarts.size();
//            reducedCount += tmpCount;
//
//            block.starts.clear();
//            block.starts.addAll(validStarts);
//
//            if (validStarts.isEmpty()) {
//                // if domain is empty, do not continue
//                return -1;
//            }
//        }
//        return reducedCount;
//    }

//    private static boolean csp(String[][] grid, int[][] varRow, int[][] varCol, Block[][] domainRow, Block[][] domainCol) {
//        if (isVarComplete(varRow) && isVarComplete(varCol)) {
//            printGrid(grid);
//            return true;
//        }
//
//        Block[][] domainRowClone = cloneDomain(domainRow);
//        Block[][] domainColClone = cloneDomain(domainCol);
//
//        if (!ac3(grid, varRow, varCol, domainRowClone, domainColClone)) {
//            return false;
//        }
//
//        Selection sel = selectVar(varRow, varCol, domainRowClone, domainColClone);
//
//        int[][] var = sel.isRow ? varRow : varCol;
//        Block[][] domain = sel.isRow ? domainRowClone : domainColClone;
//        Block block = domain[sel.index][sel.blockIndex];
//
//        boolean foundAtLeastOne = false;
//        NavigableMap<Integer, Map<Integer, List<Block[][]>>> leastConstValues = new TreeMap<>();
//        for (int value : block.starts) {
//            String[][] gridBackup = cloneGrid(grid);
//            if (!assign(sel, var, grid, value)) {
//                continue;
//            }
//
//            Block[][] domainRowBackup = cloneDomain(domainRowClone);
//            Block[][] domainColBackup = cloneDomain(domainColClone);
//
//            int rowCount = forwardChecking(false, grid, varRow, varCol, domainRowBackup, domainColBackup);
//            if (rowCount == -1) {
//                continue;
//            }
//
//            int colCount = forwardChecking(true, grid, varRow, varCol, domainRowBackup, domainColBackup);
//            if (colCount == -1) {
//                continue;
//            }
//
//            int count = rowCount + colCount;
//            if (!leastConstValues.containsKey(count)) {
//                leastConstValues.put(count, new TreeMap<>());
//            }
//            Map<Integer, List<Block[][]>> values = leastConstValues.get(count);
//            if (!values.containsKey(value)) {
//                values.put(value, new ArrayList<>());
//            }
//
//            List<Block[][]> valueDomains = values.get(value);
//            valueDomains.add(domainRowBackup);
//            valueDomains.add(domainColBackup);
//
//            unassign(sel, var);
//            grid = gridBackup;
//        }
//
//        for (Map.Entry<Integer, Map<Integer, List<Block[][]>>> leastConstValueMap : leastConstValues.entrySet()) {
//            Map<Integer, List<Block[][]>> map = leastConstValueMap.getValue();
//            for (Map.Entry<Integer, List<Block[][]>> value : map.entrySet()) {
//                Block[][] domainRowBackup = value.getValue().get(0);
//                Block[][] domainColBackup = value.getValue().get(1);
//
//                String[][] gridBackup = cloneGrid(grid);
//                if (!assign(sel, var, grid, value.getKey())) {
//                    continue;
//                }
//                foundAtLeastOne = foundAtLeastOne | csp(grid, varRow, varCol, domainRowBackup, domainColBackup);
//
//                unassign(sel, var);
//                grid = gridBackup;
//            }
//        }
//
//        return foundAtLeastOne;
//    }

    private static boolean forwardChecking(boolean checkRowVar, String[][] grid, int[][] varRow, int[][] varCol,
                                           Block[][] domainRow, Block[][] domainCol) {

        int[][] var = checkRowVar ? varRow : varCol;
        int[][] transposedVar = checkRowVar ? varCol : varRow;
        Block[][] domain = checkRowVar ? domainRow : domainCol;
        Block[][] transposedDomain = checkRowVar ? domainCol : domainRow;

        for (Selection sel : findFreeVars(checkRowVar, var, domain).values()) {
            List<Integer> validStarts = new ArrayList<>();
            Block block = domain[sel.index][sel.blockIndex];

            for (int value : block.starts) {
                String[][] gridClone = cloneGrid(grid);
                if (!assign(sel, var, gridClone, value)) {
                    continue;
                }

                boolean isValid = sequenceDoesNotOverlap(var, domain, sel.index)
                        && sequenceContainsOnlyValidColors(checkRowVar, gridClone, var, domain, sel.index);

                for (int k = value; isValid && k < value + block.length; k++) {
                    isValid = sequenceContainsOnlyValidColors(!checkRowVar, gridClone, transposedVar, transposedDomain, k);
                }
                if (isValid) {
                    validStarts.add(value);
                }

                unassign(sel, var);
            }

            domain[sel.index][sel.blockIndex].starts.clear();
            domain[sel.index][sel.blockIndex].starts.addAll(validStarts);

            if (validStarts.isEmpty()) {
                // if domain is empty, do not continue
                return false;
            }
        }
        return true;
    }

    private static boolean csp(String[][] grid, int[][] varRow, int[][] varCol, Block[][] domainRow, Block[][] domainCol) {
        if (isVarComplete(varRow) && isVarComplete(varCol)) {
            printGrid(grid);
            return true;
        }

        Block[][] domainRowClone = cloneDomain(domainRow);
        Block[][] domainColClone = cloneDomain(domainCol);

        if (!ac3(grid, varRow, varCol, domainRowClone, domainColClone)) {
            return false;
        }

        Selection sel = selectVar(varRow, varCol, domainRowClone, domainColClone);

        int[][] var = sel.isRow ? varRow : varCol;
        Block[][] domain = sel.isRow ? domainRowClone : domainColClone;
        Block block = domain[sel.index][sel.blockIndex];

        boolean foundAtLeastOne = false;
        for (int value : block.starts) {
            String[][] gridBackup = cloneGrid(grid);
            if (!assign(sel, var, grid, value)) {
                continue;
            }

            Block[][] domainRowBackup = cloneDomain(domainRowClone);
            Block[][] domainColBackup = cloneDomain(domainColClone);
            boolean isValid = forwardChecking(false, grid, varRow, varCol, domainRowBackup, domainColBackup);
            isValid = isValid && forwardChecking(true, grid, varRow, varCol, domainRowBackup, domainColBackup);

            if (isValid) {
                foundAtLeastOne = foundAtLeastOne | csp(grid, varRow, varCol, domainRowBackup, domainColBackup);
            }

            unassign(sel, var);
            grid = gridBackup;
        }

        return foundAtLeastOne;
    }


    private static Block[][] createDomain(int count, int transposedCount, Scanner sc) {
        Block[][] domain = new Block[count][];

        for (int i = 0; i < count; i++) {
            List<Block> tmpDomain = new ArrayList<>();

            String[] constraint = sc.nextLine().split(",");
            List<String> colors = new ArrayList<>();
            List<Integer> leftMost = new ArrayList<>();
            List<Integer> rightMost = new ArrayList<>();
            List<Integer> lengths = new ArrayList<>();

            int sum = 0;
            String previousColor = "";
            for (int j = 0; j < constraint.length - 1; j += 2) {
                if (!previousColor.isEmpty() || previousColor.equals(constraint[j])) {
                    sum++;
                }
                leftMost.add(sum);
                colors.add(constraint[j]);

                int blockLength = Integer.parseInt(constraint[j + 1]);
                lengths.add(blockLength);

                sum += blockLength;
            }
            sum = transposedCount;
            for (int j = constraint.length - 2; j >= 0; j -= 2) {
                sum -= Integer.parseInt(constraint[j + 1]);
                rightMost.add(0, sum);
            }

            // create blocks
            for (int j = 0; j < colors.size(); j++) {
                List<Integer> blockPositions = new ArrayList<>();
                String color = colors.get(j);

                for (int k = leftMost.get(j); k <= rightMost.get(j); k++) {
                    blockPositions.add(k);
                }
                tmpDomain.add(new Block(color, blockPositions, lengths.get(j)));
            }

            domain[i] = tmpDomain.toArray(new Block[tmpDomain.size()]);
        }
        return domain;
    }

    private static int[][] initVar(int count, Block[][] domain) {
        int[][] var = new int[count][];
        for (int i = 0; i < count; i++) {
            var[i] = new int[domain[i].length];
            for (int j = 0; j < var[i].length; j++) {
                var[i][j] = -1;
            }
        }
        return var;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] rc = sc.nextLine().split(",");
        int rows = Integer.parseInt(rc[0]);
        int cols = Integer.parseInt(rc[1]);

        Block[][] domainRow = createDomain(rows, cols, sc);
        Block[][] domainCol = createDomain(cols, rows, sc);

        int[][] varRow = initVar(rows, domainRow);
        int[][] varCol = initVar(cols, domainCol);

        String[][] grid = new String[rows][cols];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = "";
            }
        }

        long startTime = System.nanoTime();
        if (!csp(grid, varRow, varCol, domainRow, domainCol)) {
            System.out.println("null");
        }
        long endTime = System.nanoTime();

        System.out.println("TIME : " + TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
    }
}