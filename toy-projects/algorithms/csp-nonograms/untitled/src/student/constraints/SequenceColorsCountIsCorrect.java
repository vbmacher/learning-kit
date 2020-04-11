package student.constraints;

import student.Block;
import student.Constraint;
import student.GridDimension;
import student.Selection;

import java.util.HashMap;
import java.util.Map;

public class SequenceColorsCountIsCorrect implements Constraint {

    @Override
    public boolean check(Selection sel, String[][] grid, InconsistencyCatch inconsistencyCatch) {
        Map<String, Integer> blockColorLengths = new HashMap<>();
        for (Block block : sel.blocksPerIndex()) {
            if (blockColorLengths.containsKey(block.color)) {
                blockColorLengths.put(block.color, blockColorLengths.get(block.color) + block.length);
            } else {
                blockColorLengths.put(block.color, block.length);
            }
        }

        GridDimension dim = sel.toGridDimension(0);
        Map<String, Integer> gridColorsLength = new HashMap<>();
        while (dim.isValid(grid)) {
            String color = dim.cell(grid);
            if (!color.isEmpty()) {
                if (gridColorsLength.containsKey(color)) {
                    gridColorsLength.put(color, gridColorsLength.get(color) + 1);
                } else {
                    gridColorsLength.put(color, 1);
                }
            }
            dim.advance();
        }

        for (Map.Entry<String, Integer> entry : gridColorsLength.entrySet()) {
            String gridColor = entry.getKey();
            int gridColorLength = entry.getValue();

            if (!blockColorLengths.containsKey(gridColor) || gridColorLength > blockColorLengths.get(gridColor)) {
                return false;
            }
        }
        return true;
    }
}
