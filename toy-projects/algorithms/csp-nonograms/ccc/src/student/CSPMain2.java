package student;

public class CSPMain2 {
//
//    static class Block {
//        private final List<Integer> positions;
//        private final int len;
//        private final String color;
//
//        Block(String color, List<Integer> positions, int len) {
//            this.color = color;
//            this.positions = positions;
//            this.len = len;
//        }
//    }
//
//    // Premenne:
//    // row 0: startBlock01, startBlock02, ... , startBlock0N
//    // row 1: startBlock11, startBlock12, ... , startBlock1M
//    // ...
//
//    private static boolean isComplete(int[][] ass) {
//        for (int[] rowBlocks : ass) {
//            for (int block : rowBlocks) {
//                if (block == -1) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    private static void printSolution(int[][] ass, int cols, List<Block>[] rowDomain) {
//        for (int i = 0; i < ass.length; i++) {
//            List<Block> blocks = rowDomain[i];
//
//            int consolePos = 0;
//            for (int j = 0; j < ass[i].length; j++) {
//                Block rowBlock = blocks.get(j);
//                int start = ass[i][j];
//
//                while (consolePos < start) {
//                    System.out.print("_");
//                    consolePos++;
//                }
//                for (int k = 0; k < rowBlock.len; k++) {
//                    System.out.print(rowBlock.color);
//                    consolePos++;
//                }
//            }
//            while (consolePos < cols) {
//                System.out.print("_");
//                consolePos++;
//            }
//            System.out.println();
//        }
//    }
//
//    private static Point selectVariable(int[][] ass) {
//        // TODO: most-constrained; most-constraining
//        for (int i = 0; i < ass.length; i++) {
//            for (int j = 0; j < ass[i].length; j++) {
//                if (ass[i][j] == -1) {
//                    return new Point(i,j);
//                }
//            }
//        }
//        return null;
//    }
//
//    private static boolean isValid(int[][] ass, List<Block>[] rowDomain, List<Block>[] colDomain) {
//
//
//        for (int i = 0; i < ass.length; i++) {
//            for (int j = 0; j < ass[i].length; j++) {
//                if (ass[i][j] == -1) {
//                    continue;
//                }
//                Block rowBlock = rowDomain[i].get(j);
//
//                int rowBlockPos = ass[i][j];
//                String rowBlockColor = rowBlock.color;
//
//                for (int k = rowBlockPos; k < rowBlock.len; k++) {
//                    boolean valid = false;
//                    List<Block> colBlocks = colDomain[k];
//                    for (int l = 0; l < colBlocks.size(); l++) {
//                        Block colBlock = colBlocks.get(l);
//
//                        for (int colPos : colBlock.positions) {
//                            if (colPos == i && colBlock.color.equals(rowBlockColor)) {
//                                valid = true;
//                                break;
//                            }
//                            if (colPos < i && colPos + colBlock.len > rowBlockPos && colBlock.color.equals(rowBlockColor)) {
//                                valid = true;
//                                break;
//                            }
//                        }
//                    }
//                    if (!valid) {
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//
//    private static boolean csp(int[][] ass, List<Block>[] rowDomain, List<Block>[] colDomain) {
//        if (isComplete(ass)) {
//            printSolution(ass, colDomain.length, rowDomain);
//            return true;
//        }
//
//        Point selectedVariable = selectVariable(ass);
//        for (int value : rowDomain[selectedVariable.x].get(selectedVariable.y).positions) {
//            ass[selectedVariable.x][selectedVariable.y] = value;
//            if (isValid(ass, rowDomain, colDomain)) {
//                if (csp(ass, rowDomain, colDomain)) {
//                    return true;
//                }
//            }
//            ass[selectedVariable.x][selectedVariable.y] = -1;
//        }
//        return false;
//    }
//
//    private static void createDomains(int count, int theOtherCount, List<Block>[] domain, Scanner sc) {
//        for (int i = 0; i < count; i++) {
//            domain[i] = new ArrayList<>();
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
//                domain[i].add(new Block(color, blockPositions, lengths.get(j)));
//            }
//        }
//    }
//
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//
//        String[] rc = sc.nextLine().split(",");
//        int rows = Integer.parseInt(rc[0]);
//        int cols = Integer.parseInt(rc[1]);
//
//        List<Block>[] rowDomain = new List[rows];
//        List<Block>[] colDomain = new List[cols];
//
//        createDomains(rows, cols, rowDomain, sc);
//        createDomains(cols, rows, colDomain, sc);
//
//        int[][] ass = new int[rows][];
//        for (int i = 0; i < rows; i++) {
//            ass[i] = new int[rowDomain[i].size()];
//            for (int j = 0; j < ass[i].length; j++) {
//                ass[i][j] = -1;
//            }
//        }
//
//        csp(ass, rowDomain, colDomain);
//    }

}
