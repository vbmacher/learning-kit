package student.constraints;

import student.Block;
import student.Constraint;
import student.GridDimension;
import student.Selection;

import java.util.HashMap;
import java.util.Map;

public class BlockLenCorrespondsToMaxBlockLen implements Constraint {

    private Map<String, Integer> findMaxBlockColorLengths(Selection sel) {
        Map<String, Integer> maxBlockColorLengths = new HashMap<>();
        for (Block block : sel.blocksPerIndex()) {
            if (maxBlockColorLengths.containsKey(block.color)) {
                maxBlockColorLengths.put(block.color, Math.max(maxBlockColorLengths.get(block.color), block.length));
            } else {
                maxBlockColorLengths.put(block.color, block.length);
            }
        }
        return maxBlockColorLengths;
    }

    @Override
    public boolean check(Selection sel, String[][] grid, InconsistencyCatch inconsistencyCatch) {
        Map<String, Integer> maxBlockColorLengths = findMaxBlockColorLengths(sel);

        int currentBlockLength = 0;
        String colorTrail = "";
        GridDimension dim = sel.toGridDimension(0);
        while (dim.isValid(grid)) {
            String color = dim.cell(grid);

            if (!color.isEmpty() && (colorTrail.isEmpty() || color.equals(colorTrail))) {
                currentBlockLength++;
                if (colorTrail.isEmpty()) {
                    colorTrail = color;
                }
            } else if (!colorTrail.isEmpty() && color.isEmpty()) {
                // some block has ended
                if (maxBlockColorLengths.get(colorTrail) < currentBlockLength) {
                    return false;
                }
                colorTrail = "";
                currentBlockLength = 0;
            } else if (!colorTrail.isEmpty() && !color.isEmpty()) {
                // block has changed to different one
                if (maxBlockColorLengths.get(colorTrail) < currentBlockLength) {
                    return false;
                }
                colorTrail = color;
                currentBlockLength = 1;
            }

            if (!colorTrail.isEmpty()) {
                if (!maxBlockColorLengths.containsKey(colorTrail) || maxBlockColorLengths.get(colorTrail) < currentBlockLength) {
                    return false;
                }
            }

            dim.advance();
        }
        return true;
    }
}
