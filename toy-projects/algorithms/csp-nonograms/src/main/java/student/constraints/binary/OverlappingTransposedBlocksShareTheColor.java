package student.constraints.binary;

import student.BinaryConstraint;
import student.Selection;

public class OverlappingTransposedBlocksShareTheColor implements BinaryConstraint {

    @Override
    public boolean check(Selection x, Selection y) {
        if (x.dimension == y.dimension) {
            return true;
        }

        int xStart = x.value();
        int yStart = y.value();

        if (xStart == -1 || yStart == -1) {
            return true;
        }

        int xEnd = xStart + x.block().length;
        int yEnd = yStart + y.block().length;

        if (xStart <= y.index && xEnd >= y.index) {
            return x.block().color.equals(y.block().color);
        }
        if (yStart <= x.index && yEnd >= x.index) {
            return x.block().color.equals(y.block().color);
        }

//        boolean t = !(xStart <= y.index && xEnd >= y.index && yStart <= x.index && yEnd >= x.index)
//                || x.block().color.equals(y.block().color);
//
//        System.out.println("\tSHARE C: FALSE");

        return false;

    }
}
