package student.constraints.binary;

import student.BinaryConstraint;
import student.Selection;

public class DoNotOverlap implements BinaryConstraint {

    @Override
    public boolean check(Selection x, Selection y) {
        if (x.dimension != y.dimension) {
            return true;
        }

        int xStart;
        int yStart;

        int xLen = x.block().length;
        int yLen = y.block().length;
        if (x.dimension == 0) {
            xStart = x.toGridDimension(x.value()).col;
            yStart = y.toGridDimension(y.value()).col;
        } else {
            xStart = x.toGridDimension(x.value()).row;
            yStart = y.toGridDimension(y.value()).row;
        }

        return xStart + xLen < yStart || yStart + yLen < xStart ||
                !x.block().color.equals(y.block().color) && (xStart + xLen == yStart || yStart + yLen == xStart);
    }
}
