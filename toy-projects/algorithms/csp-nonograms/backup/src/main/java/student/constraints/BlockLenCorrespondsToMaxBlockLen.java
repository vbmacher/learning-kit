package student.constraints;

import student.Constraint;
import student.GridDimension;
import student.Selection;

import java.util.Map;

public class BlockLenCorrespondsToMaxBlockLen implements Constraint {

    @Override
    public boolean check(Selection sel, String[][] grid) {
        Map<String, Integer> maxBlockColorLengths = sel.maxBlockColorLengths();

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
