package student;

/**
 * Variables:
 * int[i][j] rowSetting - i: row index, j: row block index. Value is start position of the row block
 * int[i][j] colSetting - i: col index, j: col block index. Value is start position of the col block
 * String[i][j] grid - i: row index, j: col index. Value is the color of the cell(i,j). This is just auxiliary variable.
 *
 * Domains:
 *
 *
 * Constraints:
 * 1. sequenceDoesNotOverlap:
 *     a) for all i in rows:
 *            for all *defined* row blocks k, k + 1:
 *                rowSetting[i][k] + rowBlockLength[i][k] < rowSetting[i][k + 1]     (if blocks k,k+1 have the same color, then <=)
 *
 *     b) for all i in cols:
 *            for all *defined* col blocks k, k + 1:
 *                colSetting[i][k] + colBlockLength[i][k] < colSetting[i][k + 1]    (if blocks k,k+1 have the same color, then <=)
 *
 * 2. sequenceContainsJustCorrectBlocks
 *    - all space before first block (if defined) start must be empty in grid
 *    - all space between defined subsequent blocks must be empty in grid
 *    - all space after last block (if defined) must be empty in grid
 *
 *    Note: If between two blocks exist undefined blocks, the space between them is not checked. Also, if the first
 *          block is not defined, the initial space is not checked; and if the last block is not defined - space after
 *          the block is not checked.
 *
 *
 *
 */
public class CSPMain4 {
//    static class Block {
//        final List<Integer> starts;
//        final int length;
//        final String color;
//
//        Block(String color, List<Integer> starts, int length) {
//            this.color = color;
//            this.starts = starts;
//            this.length = length;
//        }
//
//        Block copy() {
//            return new Block(color, new ArrayList<>(starts), length);
//        }
//    }
//
//    static class SelectedVar {
//        private final int x;
//        private final int y;
//        private final Block block;
//        private final boolean isRow;
//
//        SelectedVar(int x, int y, Block block, boolean isRow) {
//            this.x = x;
//            this.y = y;
//            this.block = block;
//            this.isRow = isRow;
//        }
//    }
//
//    private static boolean isComplete(int[][] setting) {
//        for (int i = 0; i < setting.length; i++) {
//            for (int j = 0; j < setting[i].length; j++) {
//                if (setting[i][j] == -1) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    public static void printGrid(String[][] grid) {
//        for (int i = 0; i < grid.length; i++) {
//            for (int j = 0; j < grid[i].length; j++) {
//                if (grid[i][j].isEmpty()) {
//                    System.out.print("_");
//                } else {
//                    System.out.print(grid[i][j]);
//                }
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }
//
//    private static SelectedVar selectSingleVar(int[][] setting, Block[][] domain, boolean rowBlock) {
//        NavigableMap<Integer, SelectedVar> freeVars = new TreeMap<>(); // sorted variables by domain length;
//
//        for (int i = 0; i < setting.length; i++) {
//            for (int j = 0; j < setting[i].length; j++) {
//                if (setting[i][j] == -1) {
//                    Block block = domain[i][j];
//                    if (!freeVars.containsKey(block.starts.size())) {
//                        freeVars.put(block.starts.size(), new SelectedVar(i, j, block, rowBlock));
//                    }
//                }
//            }
//        }
//
//        return freeVars.isEmpty() ? null : freeVars.firstEntry().getValue();
//    }
//
//    private static SelectedVar selectVar(int[][] rowSetting, int[][] colSetting, Block[][] rowDomain,
//                                         Block[][] colDomain) {
//        SelectedVar rowVar = selectSingleVar(rowSetting, rowDomain, true);
//        SelectedVar colVar = selectSingleVar(colSetting, colDomain, false);
//
//        SelectedVar selectedVar = rowVar;
//        if (rowVar != null && colVar != null) {
//            if (rowVar.block.starts.size() < colVar.block.starts.size()) {
//                selectedVar = rowVar;
//            } else {
//                selectedVar = colVar;
//            }
//        }
//
//        if (selectedVar == null) {
//            selectedVar = selectSingleVar(colSetting, colDomain, false);
//        }
//        return selectedVar;
//    }
//
//    private static boolean fillBlockOnGrid(boolean row, String[][] grid, Block block, int startPosition, int rowOrCol) {
//        String color = block.color;
//        for (int k = 0; k < block.length; k++) {
//            int gridRow = row ? rowOrCol : (startPosition + k);
//            int gridCol = row ? (startPosition + k) : rowOrCol;
//
//            if (!(grid[gridRow][gridCol].isEmpty() || grid[gridRow][gridCol].equals(color))) {
//                return false;
//            }
//        }
//
//        for (int k = 0; k < block.length; k++) {
//            int gridRow = row ? rowOrCol : (startPosition + k);
//            int gridCol = row ? (startPosition + k) : rowOrCol;
//
//            grid[gridRow][gridCol] = color;
//        }
//        return true;
//    }
//
//    private static String[][] backupGrid(String[][] grid) {
//        String[][] gridBackup = new String[grid.length][grid[0].length];
//        for (int i = 0; i < gridBackup.length; i++) {
//            System.arraycopy(grid[i], 0,gridBackup[i], 0, grid[i].length);
//        }
//        return gridBackup;
//    }
//
//    private static Block[][] cloneDomain(Block[][] domain) {
//        Block[][] newDomain = new Block[domain.length][];
//        for (int i = 0; i < domain.length; i++) {
//            Block[] blocks = domain[i];
//            newDomain[i] = new Block[blocks.length];
//
//            for (int j = 0; j < blocks.length; j++) {
//                newDomain[i][j] = blocks[j].copy();
//            }
//        }
//        return newDomain;
//    }
//
//    static boolean sequenceDoesNotOverlap(int[][] setting, CSPMain4.Block[][] domain, int i) {
//      //  System.out.println("    sequenceDoesNotOverlap at " + i);
//        for (int k = 0; k < domain[i].length - 1; k++) {
//            if (setting[i][k] == -1 || setting[i][k + 1] == -1) {
//                continue;
//            }
//
//            CSPMain4.Block blockK = domain[i][k];
//            CSPMain4.Block blockKplusOne = domain[i][k + 1];
//
//            if (blockK.color.equals(blockKplusOne.color)) {
//                if (setting[i][k] + blockK.length >= setting[i][k + 1]) {
//    //                System.out.println("      " + i + "," + k + " | FALSE (eq. color)");
//                    return false;
//                }
//            } else {
//                if (setting[i][k] + blockK.length > setting[i][k + 1]) {
//       //             System.out.println("      " + i + "," + k + " | FALSE");
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//
//    static boolean sequenceContainsJustCorrectBlocks(boolean row, String[][] grid, int[][] setting,
//                                                     CSPMain4.Block[][] domain,
//                                                     Collection<Integer> settingIndices) {
//      //  System.out.println("    sequenceContainsJustCorrectBlocks(row=" + row + ") at " + settingIndices);
//
//        for (int i : settingIndices) {
//            int start = 0;
//            int lastK = -1;
//
//            for (int k = 0; k < setting[i].length; k++) {
//                if (setting[i][k] != -1) {
//                    while (start < setting[i][k]) {
//                        int gridRow = row ? i : start;
//                        int gridCol = row ? start : i;
//
//                        if (((k == 0) || (lastK == k - 1)) && !grid[gridRow][gridCol].isEmpty()) {
//                  //          System.out.println("      " + i + "," + k + " | FALSE (grid at " + gridRow + "," + gridCol + ")");
//                            return false;
//                        }
//                        start++;
//                    }
//                    start += domain[i][k].length;
//                    lastK = k;
//                }
//            }
//            int until = row ? grid[i].length : grid.length;
//            while (start > 0 && (lastK == setting[i].length - 1) && start < until) {
//                int gridRow = row ? i : start;
//                int gridCol = row ? start : i;
//
//                if (!grid[gridRow][gridCol].isEmpty()) {
//     //               System.out.println("      FALSE (grid at " + gridRow + "," + gridCol + ")");
//                    return false;
//                }
//                start++;
//            }
//        }
//        return true;
//    }
//
//    static boolean blockIsValidInTransposedDomain(boolean row, final int i, final int j, String[][] grid,
//                                                  CSPMain4.Block[][] otherDomain) {
//
//        // basically sequenceContainsJustCorrectBlocks for unassigned fields
//        int checkedBlockStart = i;
//        int checkedBlockEnd = checkedBlockStart;
//        String color = row ? grid[i][j] : grid[j][i];
//
//        int gridX = row ? checkedBlockStart - 1 : j;
//        int gridY = row ? j : checkedBlockStart - 1;
//
//        System.out.println("        a) blockStart = " + checkedBlockStart + ", blockEnd = " + checkedBlockEnd);
//        while (gridX >=0 && gridY >= 0 && grid[gridX][gridY].equals(color)) {
//            checkedBlockStart--;
//
//            gridX = row ? checkedBlockStart - 1 : j;
//            gridY = row ? j : checkedBlockStart - 1;
//        }
//
//        gridX = row ? checkedBlockEnd + 1 : j;
//        gridY = row ? j : checkedBlockEnd + 1;
//        System.out.println("        b) blockStart = " + checkedBlockStart + ", blockEnd = " + checkedBlockEnd);
//
//        if (!row && gridX < grid.length && gridY < grid[gridX].length) {
//            System.out.println("          grid[" + gridX + "][" + gridY + "] = " + grid[gridX][gridY] + "; color=" + color);
//        }
//
//        while (gridX < grid.length && gridY < grid[gridX].length && grid[gridX][gridY].equals(color)) {
//            checkedBlockEnd++;
//
//            gridX = row ? checkedBlockEnd + 1 : j;
//            gridY = row ? j : checkedBlockEnd + 1;
//        }
//        System.out.println("        c) blockStart = " + checkedBlockStart + ", blockEnd = " + checkedBlockEnd);
//
//
//        // now check if that "block" exist in the transposed domain
//        int checkedBlockLen = checkedBlockEnd - checkedBlockStart + 1;
//        System.out.println("    Transposed block | " + i + "," + j + " | " + checkedBlockStart + "-" + checkedBlockEnd + " | len=" + checkedBlockLen);
//        for (int k = 0; k < otherDomain[j].length; k++) {
//            CSPMain4.Block block = otherDomain[j][k];
//            System.out.println("      checking block " + block.starts);
//            if (block.starts.contains(checkedBlockStart) && block.length >= checkedBlockLen && block.color.equals(color)) {
//                return true;
//            }
//        }
//        System.out.println("      FALSE");
//        //  printGrid(grid);
//        return false;
//    }
//
//    private static Block[][] forwardChecking(String[][] grid, int[][] rowSetting, int[][] colSetting,
//                                             Block[][] rowDomain, Block[][] colDomain, boolean row) {
//
//        int[][] setting = row ? rowSetting : colSetting;
//        int[][] otherSetting = row ? colSetting : rowSetting;
//        Block[][] domain = row ? rowDomain : colDomain;
//        Block[][] otherDomain = row ? colDomain : rowDomain;
//
//        Block[][] newDomain = cloneDomain(domain);
//
//        List<Point> freeVars = new ArrayList<>();
//        for (int i = 0; i < setting.length; i++) {
//            for (int j = 0; j < setting[i].length; j++) {
//                if (setting[i][j] == -1) {
//                    freeVars.add(new Point(i,j));
//                }
//            }
//        }
//
//        for (Point freeVar : freeVars) {
//            List<Integer> validStarts = new ArrayList<>();
//            Block block = domain[freeVar.x][freeVar.y];
//
//            for (int value : block.starts) {
//                // try to set this value and check constraints
//                setting[freeVar.x][freeVar.y] = value;
//                String[][] backup = backupGrid(grid);
//                if (!fillBlockOnGrid(row, backup, block, value, freeVar.x)) {
//                    setting[freeVar.x][freeVar.y] = -1;
//                    continue;
//                }
//
//            //    System.out.println("  FC | row=" + row + " | " + freeVar.x + "," + freeVar.y + " | Trying " + value);
//
//                boolean valid = sequenceDoesNotOverlap(setting, domain, freeVar.x)
//                        && sequenceContainsJustCorrectBlocks(row, backup, setting, domain, Arrays.asList(freeVar.x));
//
//                List<Integer> settingIndices = new ArrayList<>();
//                for (int k = value; k < value + block.length; k++) {
//                    settingIndices.add(k);
//                }
//                valid = valid
//                        && sequenceContainsJustCorrectBlocks(!row, backup, otherSetting, otherDomain, settingIndices)
//                      //  && blockIsValidInTransposedDomain(row, freeVar.x, value, backup, otherDomain)
//                ;
//
//
//                if (valid) {
//                //    System.out.println("    " + freeVar.x + "," + freeVar.y + " | successful value: " + value);
//                    validStarts.add(value);
//                }
//
//                setting[freeVar.x][freeVar.y] = -1;
//            }
//
//            newDomain[freeVar.x][freeVar.y].starts.clear();
//            newDomain[freeVar.x][freeVar.y].starts.addAll(validStarts);
//
//        //    System.out.println("  FC | New domain of | " + freeVar.x + "," + freeVar.y + " | " + validStarts);
//            if (validStarts.isEmpty()) {
//                return newDomain; // we can stop forward checking now.. it is not valid anyway
//            }
//        }
//        return newDomain;
//    }
//
//    private static boolean isValidDomain(int[][] setting, Block[][] domain) {
//        for (int i = 0; i < setting.length; i++) {
//            for (int j = 0; j < setting[i].length; j++) {
//                if (setting[i][j] == -1 && domain[i][j].starts.isEmpty()) {
//                  //  System.out.println("[" + i + ", " + j + "] Invalid domain");
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    private static void csp(String[][] grid, int[][] rowSetting, int[][] colSetting, Block[][] rowDomain,
//                            Block[][] colDomain) {
//        if (isComplete(rowSetting) && isComplete(colSetting)) {
//
//              //  System.out.println(rowSetting[0][0]);
//
//            printGrid(grid);
//            return;
//        }
//
//        SelectedVar selectedVar = selectVar(rowSetting, colSetting, rowDomain, colDomain);
//        if (selectedVar == null) {
//            return;
//        }
//        int varX = selectedVar.x;
//        int varY = selectedVar.y;
//
//        Block[][] domain = selectedVar.isRow ? rowDomain : colDomain;
//        Block block = domain[varX][varY];
//
//       // System.out.println("[" + varX + "," + varY + "] block " + block.starts + " , row: " + selectedVar.isRow);
//        for (int value : block.starts) {
//            int[][] setting = selectedVar.isRow ? rowSetting : colSetting;
//
//            String[][] gridBackup = backupGrid(grid);
//            setting[varX][varY] = value;
//
//            if (!fillBlockOnGrid(selectedVar.isRow, grid, block, value, varX)) {
//                setting[varX][varY] = -1;
//                continue;
//            }
//
//         //   System.out.println("row=" + selectedVar.isRow + " | " + selectedVar.x + "," + selectedVar.y + " | Selecting " + value);
//
//            Block[][] newRowDomain = null;
//            Block[][] newColDomain = null;
//
//            if (selectedVar.isRow) {
//                newRowDomain = forwardChecking(grid, rowSetting, colSetting, rowDomain, colDomain, true);
//                newColDomain = forwardChecking(grid, rowSetting, colSetting, newRowDomain, colDomain, false);
//            } else {
//                newColDomain = forwardChecking(grid, rowSetting, colSetting, rowDomain, colDomain, false);
//                newRowDomain = forwardChecking(grid, rowSetting, colSetting, rowDomain, newColDomain, true);
//            }
//
//            if (isValidDomain(rowSetting, newRowDomain) && isValidDomain(colSetting, newColDomain)) {
//                //   if (isValid(grid, rowSetting, colSetting, newRowDomain, newColDomain)) {
//                csp(grid, rowSetting, colSetting, newRowDomain, newColDomain);
//                //   }
//            }
//
//            setting[varX][varY] = -1;
//            grid = gridBackup;
//        }
//    }
//
//    private static Block[][] createDomains(int count, int theOtherCount, Scanner sc) {
//        Block[][] domain = new Block[count][];
//
//        for (int i = 0; i < count; i++) {
//            List<Block> tmpDomain = new ArrayList<>();
//
//            String[] constraint = sc.nextLine().split(",");
//            List<String> colors = new ArrayList<>();
//            List<Integer> leftMost = new ArrayList<>();
//            List<Integer> rightMost = new ArrayList<>();
//            List<Integer> lengths = new ArrayList<>();
//
//            int sum = 0;
//            String previousColor = "";
//            for (int j = 0; j < constraint.length - 1; j += 2) {
//                if (!previousColor.isEmpty() || previousColor.equals(constraint[j])) {
//                    sum++;
//                }
//                leftMost.add(sum);
//                colors.add(constraint[j]);
//
//                int length = Integer.parseInt(constraint[j + 1]);
//                lengths.add(length);
//
//                sum += length;
//            }
//            sum = theOtherCount;
//            for (int j = constraint.length - 2; j >= 0; j -= 2) {
//                sum -= Integer.parseInt(constraint[j + 1]);
//                rightMost.add(0, sum);
//            }
//
//            // create blocks
//            for (int j = 0; j < colors.size(); j++) {
//                List<Integer> blockPositions = new ArrayList<>();
//                String color = colors.get(j);
//
//                for (int k = leftMost.get(j); k <= rightMost.get(j); k++) {
//                    blockPositions.add(k);
//                }
//                tmpDomain.add(new Block(color, blockPositions, lengths.get(j)));
//            }
//
//            domain[i] = tmpDomain.toArray(new Block[tmpDomain.size()]);
//        }
//        return domain;
//    }
//
//    private static int[][] initSetting(int count, Block[][] domain) {
//        int[][] setting = new int[count][];
//        for (int i = 0; i < count; i++) {
//            setting[i] = new int[domain[i].length];
//            for (int j = 0; j < setting[i].length; j++) {
//                setting[i][j] = -1;
//            }
//        }
//        return setting;
//    }
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//
//        String[] rc = sc.nextLine().split(",");
//        int rows = Integer.parseInt(rc[0]);
//        int cols = Integer.parseInt(rc[1]);
//
//        Block[][] rowDomain = createDomains(rows, cols, sc);
//        Block[][] colDomain = createDomains(cols, rows, sc);
//
//        int[][] rowSetting = initSetting(rows, rowDomain);
//        int[][] colSetting = initSetting(cols, colDomain);
//
//        String[][] grid = new String[rows][cols];
//        for (int i = 0; i < grid.length; i++) {
//            for (int j = 0; j < grid[i].length; j++) {
//                grid[i][j] = "";
//            }
//        }
//
//        csp(grid, rowSetting, colSetting, rowDomain, colDomain);
//    }

}
