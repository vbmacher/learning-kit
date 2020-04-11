package student;

public class CSPMain3 {
//    static class Block {
//        private final List<Integer> starts;
//        private final int length;
//        private final String color;
//
//        Block(String color, List<Integer> starts, int length) {
//            this.color = color;
//            this.starts = starts;
//            this.length = length;
//        }
//    }
//
//    static class SelectedVar {
//        private final int x;
//        private final int y;
//        private final Block block;
//
//        SelectedVar(int x, int y, Block block) {
//            this.x = x;
//            this.y = y;
//            this.block = block;
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
//    private static void printGrid(String[][] grid) {
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
//    private static SelectedVar selectVariable(int[][] setting, Block[][] domain) {
//        NavigableMap<Integer, SelectedVar> freeVars = new TreeMap<>(); // sorted variables by domain length;
//
//        for (int i = 0; i < setting.length; i++) {
//            for (int j = 0; j < setting[i].length; j++) {
//                if (setting[i][j] == -1) {
//                    Block block = domain[i][j];
//                    if (!freeVars.containsKey(block.starts.size())) {
//                        freeVars.put(block.starts.size(), new SelectedVar(i, j, block));
//                    }
//                }
//            }
//        }
//
//        return freeVars.isEmpty() ? null : freeVars.firstEntry().getValue();
//    }
//
//    private static boolean sequenceDoesNotOverlap(int[][] setting, Block[][] domain) {
//        for (int i = 0; i < setting.length; i++) {
//            for (int k = 0; k < domain[i].length - 1; k++) {
//                if (setting[i][k] == -1 || setting[i][k+1] == -1) {
//                    continue;
//                }
//
//                Block blockK = domain[i][k];
//                Block blockKplusOne = domain[i][k + 1];
//
//                if (blockK.color.equals(blockKplusOne.color)) {
//                    if (setting[i][k] + blockK.length >= setting[i][k + 1]) {
//                        return false;
//                    }
//                } else {
//                    if (setting[i][k] + blockK.length > setting[i][k + 1]) {
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//    private static boolean sequenceContainsJustCorrectBlocks(boolean row, String[][] grid, int[][] setting,
//                                                             Block[][] domain) {
//        for (int i = 0; i < setting.length; i++) {
//            int start = 0;
//            int lastK = 0;
//            for (int k = 0; k < setting[i].length; k++) {
//                if (setting[i][k] != -1) {
//                    while (start < setting[i][k]) {
//                        int gridRow = row ? i : start;
//                        int gridCol = row ? start : i;
//
//                        if (!grid[gridRow][gridCol].isEmpty()) {
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
//                    return false;
//                }
//                start++;
//            }
//        }
//        return true;
//    }
//
//    private static boolean isValid(String[][] grid, int[][] rowSetting, int[][] colSetting, Block[][] rowDomain,
//                                   Block[][] colDomain) {
//        return sequenceDoesNotOverlap(rowSetting, rowDomain)
//                && sequenceDoesNotOverlap(colSetting, colDomain)
//                && sequenceContainsJustCorrectBlocks(true, grid, rowSetting, rowDomain)
//                && sequenceContainsJustCorrectBlocks(false, grid, colSetting, colDomain);
//
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
//    private static void csp(String[][] grid, int[][] rowSetting, int[][] colSetting, Block[][] rowDomain,
//                            Block[][] colDomain) {
//        if (isComplete(rowSetting) && isComplete(colSetting)) {
//            printGrid(grid);
//            return;
//        }
//
//        SelectedVar rowVar = selectVariable(rowSetting, rowDomain);
//        SelectedVar colVar = selectVariable(colSetting, colDomain);
//
//        boolean selectRow = true;
//        SelectedVar selectedVar = rowVar;
//        if (rowVar != null && colVar != null) {
//            if (rowVar.block.starts.size() < colVar.block.starts.size()) {
//                selectedVar = rowVar;
//                selectRow = true;
//            } else {
//                selectedVar = colVar;
//                selectRow = false;
//            }
//        }
//
//        if (selectedVar == null) {
//            selectRow = false;
//            selectedVar = selectVariable(colSetting, colDomain);
//        }
//        int varX = selectedVar.x;
//        int varY = selectedVar.y;
//
//        Block[][] domain = selectRow ? rowDomain : colDomain;
//        Block block = domain[varX][varY];
//
//        for (int value : block.starts) {
//            int[][] x = selectRow ? rowSetting : colSetting;
//
//            x[varX][varY] = value;
//            String[][] gridBackup = new String[rowSetting.length][colSetting.length];
//            for (int i = 0; i < gridBackup.length; i++) {
//                System.arraycopy(grid[i], 0,gridBackup[i], 0, grid[i].length);
//            }
//
//            if (!fillBlockOnGrid(selectRow, grid, block, value, varX)) {
//                x[varX][varY] = -1;
//                continue;
//            }
//
//            if (isValid(grid, rowSetting, colSetting, rowDomain, colDomain)) {
//                csp(grid, rowSetting, colSetting, rowDomain, colDomain);
//            }
//
//            x[varX][varY] = -1;
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
