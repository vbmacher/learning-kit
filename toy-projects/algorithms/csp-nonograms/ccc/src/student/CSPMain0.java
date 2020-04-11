package student;

public class CSPMain0 {
//
//    class Assignment {
//        private final AssignedBlock[][] rowX;
//        private final AssignedBlock[][] colX;
//
//        Assignment(int rows, int cols, Domains domains) {
//            rowX = new AssignedBlock[rows][];
//            colX = new AssignedBlock[cols][];
//
//            for (int i = 0; i < rowX.length; i++) {
//                rowX[i] = new AssignedBlock[domains.rowBlocks.size()];
//            }
//            for (int i = 0; i < colX.length; i++) {
//                colX[i] = new AssignedBlock[domains.colBlocks.size()];
//            }
//        }
//
//        boolean isComplete() {
//            for (AssignedBlock[] rowBlocks : rowX) {
//                for (AssignedBlock rowBlock : rowBlocks) {
//                    if (rowBlock == null) {
//                        return false;
//                    }
//                }
//            }
//            for (AssignedBlock[] colBlocks : colX) {
//                for (AssignedBlock colBlock : colBlocks) {
//                    if (colBlock == null) {
//                        return false;
//                    }
//                }
//            }
//            return true;
//        }
//
//        AssignedBlock assign() {
//            for (int i = 0; i < rowX.length; i++) {
//                boolean found = false;
//                for (int j = 0; j < rowX[i].length; j++) {
//                    if (rowX[i][j] == null) {
//                        found = true;
//                        break;
//                    }
//                }
//                if (found) {
//
//                }
//            }
//
//        }
//    }
//
//    class AssignedBlock {
//        private final Block belongsTo;
//        private final int position;
//
//        AssignedBlock(Block belongsTo, int position) {
//            this.belongsTo = Objects.requireNonNull(belongsTo);
//            this.position = position;
//        }
//
//        void returnBlock() {
//            belongsTo.froms.add(position);
//        }
//    }
//
//    class Block {
//        private final String color;
//        private final List<Integer> froms = new ArrayList<>();
//
//        Block(String color) {
//            this.color = Objects.requireNonNull(color);
//        }
//
//        boolean canAssign() {
//            return !froms.isEmpty();
//        }
//
//        AssignedBlock assign() {
//            return new AssignedBlock(this, froms.remove(0));
//        }
//
//    }
//
//    class Domains {
//        private final List<List<Block>> rowBlocks = new ArrayList<>();
//        private final List<List<Block>> colBlocks = new ArrayList<>();
//
//        AssignedBlock assignRowBlock(int row, int block) {
//
//        }
//    }
//
//
//    private boolean csp(Assignment ass, Domains domains) {
//
//    }
//
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//
//        String[] rc = sc.nextLine().split(" ");
//        int rows = Integer.parseInt(rc[0]);
//        int cols = Integer.parseInt(rc[1]);
//
//
//
//    }

}
