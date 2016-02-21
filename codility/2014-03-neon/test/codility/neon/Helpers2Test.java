package codility.neon;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class Helpers2Test {
    private SolutionTry1 solution;

    @Before
    public void setUp() {
        solution = new SolutionTry1();
    }

    @Test
    public void testCheckBoatLeftBoundary() {
        int[] R = new int[] { 0 };
        int X = 1;
        int M = 10;

        assertEquals(-1, solution.getBoatOverlap(R, X, M, 0));
    }

    @Test
    public void testCheckBoatLeftNeighbour() {
        int[] R = new int[] { 1, 2 };
        int X = 1;
        int M = 10;

        assertEquals(-1, solution.getBoatOverlap(R, X, M, 1));
    }

    @Test
    public void testCheckBoatRightBoundary() {
        int[] R = new int[] { 1 };
        int X = 1;
        int M = 2;

        assertEquals(1, solution.getBoatOverlap(R, X, M, 0));
    }

    @Test
    public void testCheckBoatRightNeighbour() {
        int[] R = new int[] { 1, 2 };
        int X = 1;
        int M = 10;

        assertEquals(1, solution.getBoatOverlap(R, X, M, 0));
    }

    @Test
    public void testCheckBoatLeftAgain() {
        int[] R = new int[] { 2, 4, 6, 7 };
        int X = 1;
        int M = 10;
        assertEquals(-1, solution.getBoatOverlap(R, X, M, 3));
    }

    @Test
    public void testMoveBoatsLeft() {
        int[] R = new int[] { 2, 4, 6, 8 };
        int X = 1;
        int M = 10;
        assertTrue(solution.moveBoatIfCan(R, X, M, 3, -X));

        assertArrayEquals(new int[] { 1, 3, 5, 7 }, R);
    }

    @Test
    public void testCannotMoveBoatsLeft() {
        int[] R = new int[] { 1, 2, 4, 6, 8 };
        int X = 1;
        int M = 10;
        assertFalse(solution.moveBoatIfCan(R, X, M, 4, -X));

        assertArrayEquals(new int[] { 1, 2, 4, 6, 8 }, R);
    }

    @Test
    public void testMoveBoatsLeftStopSomewhere() {
        int[] R = new int[] { 2, 4, 6, 8, 12 };
        int X = 1;
        int M = 14;
        assertTrue(solution.moveBoatIfCan(R, X, M, 4, -X));

        assertArrayEquals(new int[] { 2, 4, 6, 8, 11 }, R);
    }

    @Test
    public void testMoveBoatsRight() {
        int[] R = new int[] { 2, 4, 6, 8 };
        int X = 1;
        int M = 11;
        assertTrue(solution.moveBoatIfCan(R, X, M, 0, X));

        assertArrayEquals(new int[] { 3, 5, 7, 9 }, R);
    }

    @Test
    public void testCannotMoveBoatsRight() {
        int[] R = new int[] { 2, 4, 6, 8, 10 };
        int X = 1;
        int M = 12;
        assertFalse(solution.moveBoatIfCan(R, X, M, 0, X));

        assertArrayEquals(new int[] { 2, 4, 6, 8, 10 }, R);
    }

    @Test
    public void testMoveBoatsRightStopSomewhere() {
        int[] R = new int[] { 2, 6, 8, 10 };
        int X = 1;
        int M = 12;
        assertTrue(solution.moveBoatIfCan(R, X, M, 0, X));

        assertArrayEquals(new int[] { 3, 6, 8, 10 }, R);
    }

}
