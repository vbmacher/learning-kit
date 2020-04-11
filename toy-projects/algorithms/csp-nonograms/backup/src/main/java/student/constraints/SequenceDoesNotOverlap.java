package student.constraints;

import student.Block;
import student.Constraint;
import student.Selection;

public class SequenceDoesNotOverlap implements Constraint {

    @Override
    public boolean check(Selection sel, String[][] grid) {
        int blocksCount = sel.varPerIndex().length;
        int[] var = sel.varPerIndex();
        Block[] blocks = sel.blocksPerIndex();

        int startBlock = Math.max(sel.blockIndex - 1, 0);
        int stopBlock = Math.min(sel.blockIndex + 1, blocksCount - 1);
        for (int k = startBlock; k < stopBlock; k++) {
            if (var[k] == -1 || var[k + 1] == -1) {
                // if block k and (k+1) are both undefined, overlapping for k-th block must not be checked
                continue;
            }

            int blockFirstEnds = var[k] + blocks[k].length;
            int blockSecondStarts = var[k + 1];

            if (blocks[k].color.equals(blocks[k + 1].color) && (blockFirstEnds >= blockSecondStarts)) {
                return false;
            } else if (blockFirstEnds > blockSecondStarts) {
                return false;
            }
        }
        return true;
    }
}
