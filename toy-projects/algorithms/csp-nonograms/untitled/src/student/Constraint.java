package student;

public interface Constraint {

    interface InconsistencyCatch {

        void catchGridValue(GridDimension dim, String color);

        void catchSelection(Selection selection);
    }

    boolean check(Selection selection, String[][] grid, InconsistencyCatch inconsistencyCatch);

}
