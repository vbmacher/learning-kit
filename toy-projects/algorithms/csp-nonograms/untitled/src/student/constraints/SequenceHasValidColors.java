package student.constraints;

import student.Block;
import student.Constraint;
import student.GridDimension;
import student.Selection;

public class SequenceHasValidColors implements Constraint {

    @Override
    public boolean check(Selection sel, String[][] grid, InconsistencyCatch inconsistencyCatch) {
        int[] var = sel.varPerIndex();
        Block[] blocks = sel.blocksPerIndex();

        int blockStart = 0;
        int lastK = -1;

        String lastColor = "";
        for (int k = 0; k < var.length; k++) {
            if (var[k] != -1) {
                // check spaces / valid colors from start until the first block and between subsequent defined pair of blocks
                String continuousColor = "";

                GridDimension dim = sel.toGridDimension(blockStart);
                while (blockStart < var[k]) {
                    if (((k == 0) || (lastK == k - 1)) && !dim.cell(grid).isEmpty()) {
                        inconsistencyCatch.catchGridValue(dim, dim.cell(grid));
                        return false;
                    } else if (lastK != k - 1) {
                        continuousColor = dim.cell(grid);
                    }
                    blockStart++;
                    dim.advance();
                }
                blockStart += blocks[k].length;
                lastK = k;
                lastColor = blocks[k].color;
                if (continuousColor.equals(lastColor)) {
                    // block started before it should
                    inconsistencyCatch.catchGridValue(dim, dim.cell(grid));
                    return false;
                }
            } else if (blockStart > 0 && !lastColor.isEmpty()) {
                GridDimension dim = sel.toGridDimension(blockStart);
                if (dim.cell(grid).equals(lastColor)) {
                    // block ended but still continues with the same color...
                    inconsistencyCatch.catchGridValue(dim, dim.cell(grid));
                    return false;
                } else {
                    lastColor = "";
                }
            }
        }

        GridDimension dim = sel.toGridDimension(blockStart);
        while (blockStart > 0 && (lastK == var.length - 1) && blockStart <= dim.until(grid)) {
            if (!dim.cell(grid).isEmpty()) {
                inconsistencyCatch.catchGridValue(dim, dim.cell(grid));
                return false;
            }
            blockStart++;
            dim.advance();
        }
        return true;
    }

}
