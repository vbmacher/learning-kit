package student;

import java.util.Map;
import java.util.Objects;

public class Selection {
    public final int dimension;
    public final int index;
    public final int blockIndex;
    final Assignment assignment;

    Selection(int dimension, int index, int blockIndex, Assignment assignment) {
        this.index = index;
        this.blockIndex = blockIndex;
        this.dimension = dimension;
        this.assignment = Objects.requireNonNull(assignment);
    }

    public GridDimension toGridDimension(int value) {
        return new GridDimension(this, value);
    }

    public Block block() {
        return assignment.blocks[dimension][index][blockIndex];
    }

    public int[] varPerIndex() {
        return assignment.variables[dimension][index];
    }

    int[] transposedVarPerIndex(int transposedIndex) {
        int transposedDimension = (dimension == 0) ? 1 : 0;
        return assignment.variables[transposedDimension][transposedIndex];
    }

    public Block[] blocksPerIndex() {
        return assignment.blocks[dimension][index];
    }

    public Map<String, Integer> blockColorLenghts() {
        return assignment.blockColorLengths[dimension][index];
    }

    public Map<String, Integer> maxBlockColorLengths() {
        return assignment.maxBlockColorLengths[dimension][index];
    }

    Selection transposed(int transposedIndex, int transposedBlockIndex) {
        int transposedDimension = (dimension == 0) ? 1 : 0;
        return new Selection(transposedDimension, transposedIndex, transposedBlockIndex, assignment);
    }

    public int value() {
        return assignment.variables[dimension][index][blockIndex];
    }

    @Override
    public String toString() {
        return "Selection{" +
                "dimension=" + dimension +
                ", index=" + index +
                ", blockIndex=" + blockIndex +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Selection selection = (Selection) o;

        if (dimension != selection.dimension) return false;
        if (index != selection.index) return false;
        return blockIndex == selection.blockIndex;
    }

    @Override
    public int hashCode() {
        int result = dimension;
        result = 31 * result + index;
        result = 31 * result + blockIndex;
        return result;
    }
}
