package student;

public class GridDimension {
    public int row;
    public int col;
    public final Selection selection;

    GridDimension(Selection selection, int value) {
        this.selection = selection;
        this.row = selection.dimension == 0 ? selection.index : value;
        this.col = selection.dimension == 0 ? value : selection.index;
    }

    public void advance() {
        if (selection.dimension == 0) {
            col++;
        } else {
            row++;
        }
    }

    public void reset(int value) {
        if (selection.dimension == 0) {
            col = value;
        } else {
            row = value;
        }
    }

    public <T> int until(T[][] grid) {
        return (selection.dimension == 0) ? grid[row].length - 1 : grid.length - 1;
    }

    public <T> T cell(T[][] grid) {
        return grid[row][col];
    }

    <T> void set(T[][] grid, T value) {
        grid[row][col] = value;
    }

    public <T> boolean isValid(T[][] grid) {
        int until = until(grid);
        return (selection.dimension == 0) ? col <= until : row <= until;
    }
}
