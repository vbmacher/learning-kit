package pkg2014.sulphur;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SolutionTest {
    private Solution solution;
    private int[] A;
    private int[] B;
    private int[] C;
    private int expected;

    @Before
    public void setUp() {
        solution = new Solution();
    }

    @After
    public void tearDown() {
        int result = solution.solution(A, B, C);
        assertEquals(expected, result);
    }

    @Test
    public void test01() {
        A = new int[] { 5, 3, 6, 3, 3 };
        B = new int[] { 2, 3, 1, 1, 2 };
        C = new int[] { -1, 0, -1, 0, 3 };
        expected = 3;
    }

    @Test
    public void test02() {
        A = new int[] { 4, 3, 1 };
        B = new int[] { 2, 2, 1 };
        C = new int[] {-1, 0, 1 };
        expected = 2;
    }

    @Test
    public void test03() {
        A = new int[] { 9, 6, 6, 10, 7, 7, 7, 7, 6, 8 };
        B = new int[] { 2, 4, 3, 3, 2, 5, 2, 3, 3, 5 };
        C = new int[] { -1, 0, -1, 1, 0, 2, 0, -1, 4, 8 };
        expected = 3;
    }

    @Test
    public void test04() {
        A = new int[] { 7, 9, 5, 8, 8, 6, 8, 5, 10, 6 };
        B = new int[] { 3, 3, 2, 4, 1, 3, 3, 4, 1, 5 };
        C = new int[] { -1, -1, 0, -1, 3, 1, 2, 4, -1, -1 };
        expected = 6;
    }
    @Test
    public void test05() {
        A = new int[]{6, 7, 10, 7, 9, 10, 10, 8, 6, 10};
        B = new int[]{1, 2, 4, 1, 4, 5, 5, 3, 5, 5};
        C = new int[]{-1, -1, -1, 2, -1, 3, 1, 2, 5, 1};
        expected = 7;
    }

    @Test
    public void test06() {
        A = new int[] { 7, 7, 10, 7, 6, 6, 8, 5, 10, 7 };
        B = new int[] { 2, 1, 4, 4, 1, 4, 1, 2, 3, 1 };
        C = new int[] { -1, -1, -1, 1, 2, 4, 2, -1, 7, 6 };
        expected = 9;
    }
}
