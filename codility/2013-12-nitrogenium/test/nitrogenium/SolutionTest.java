package nitrogenium;

import java.util.Arrays;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Before;
import org.junit.Test;

public class SolutionTest {
    private Solution solution;

    @Before
    public void setUp() {
        solution = new Solution();
    }

    @Test
    public void test01() {
        int[] A = new int[] { 2, 1, 3, 2, 3 };
        int[] B = new int[] { 0, 1, 2, 3, 1 };

        int[] result = new int[] { 1, 2, 2, 0, 2 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test02() {
        int[] A = new int[] { 1, 2, 2, 1, 1, 2 };
        int[] B = new int[] { 0, 1, 2, 3, 4, 1 };

        int[] result = new int[] { 1, 2, 0, 0, 0, 2 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test03() {
        int[] A = new int[] { 1, 100000, 1 };
        int[] B = new int[] { 0, 1, 99999, 100000 };

        int[] result = new int[] { 1, 1, 1, 0 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test04() {
        int[] A = new int[] { 1, 3, 2 };
        int[] B = new int[] { 0, 1, 2, 3, 4 };

        int[] result = new int[] { 1, 1, 1, 0, 0 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test05() {
        int[] A = new int[] { 5 };
        int[] B = new int[] { 4, 5, 6 };

        int[] result = new int[] { 1, 0, 0 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test06() {
        int[] A = new int[] { 4, 5, 8, 5, 1, 4, 6, 8, 7, 2, 2, 5 };
        int[] B = new int[] { 9, 9, 4 };

        int[] result = new int[] { 0, 0, 3 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test07() {
        int[] A = new int[] { 4, 7, 6, 4, 5, 5, 4, 2, 6 };
        int[] B = new int[] { 4 };

        int[] result = new int[] { 3 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test08() {
        int[] A = new int[] { 3,5,7,86,4,3,5,6,54,3,2,4,5,5,7,8,9,3,2,1,1,1,2,3,4,5,5,5,56,6,67,7,8,88,8,99,9,99 };
        int[] B = new int[] { 2,3,45,6,7,7,6,5,33,4,56,66 };

        int[] result = new int[] { 3, 4, 7, 5, 6, 6, 5, 4, 7, 4, 5, 5};
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test09() {
        int[] A = new int[] { 1,2,3,4,5,6,7,8,9,8,7,6,5,4,3,2,1 };
        int[] B = new int[] { 2,3,4,5,6,7,8 };

        int[] result = new int[] { 1, 1, 1, 1, 1, 1, 1};
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test10() {
        int[] A = new int[] { 3, 2, 1 };
        int[] B = new int[] { 0, 1, 2, 3, 4 };

        int[] result = new int[] { 1, 1, 1, 0, 0};
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test11() {
        int[] A = new int[] { 0 };
        int[] B = new int[] { 0 };

        int[] result = new int[] { 0 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test12() {
        int[] A = new int[] { 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000 };
        int[] B = new int[] { 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999, 100000, 99999 };

        int[] result = new int[] { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test13() {
        int[] A = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        int[] B = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

        int[] result = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test14() {
        int[] A = new int[] { 4, 5, 8, 5, 1, 3, 6, 8, 7, 2, 2, 5 };
        int[] B = new int[] { 9, 9, 4 };

        int[] result = new int[] { 0, 0, 3 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test15() {
        int[] A = new int[] { 9, 6, 10, 10, 1, 9, 6, 10, 2, 2, 8, 6 };
        int[] B = new int[] { 3, 9, 2 };

        int[] result = new int[] { 3, 2, 3 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test16() {
        int[] A = new int[] { 10, 0, 10};
        int[] B = new int[] { 3, 0, 2 };

        int[] result = new int[] { 2, 2, 2 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test17() {
        int[] A = new int[] { 0, 10, 0};
        int[] B = new int[] { 3, 11, 10 };

        int[] result = new int[] { 1, 0, 0 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

    @Test
    public void test18() {
        int[] A = new int[] { 3, 1, 2, 1, 2 };
        int[] B = new int[] { 3, 2, 1 };

        int[] result = new int[] { 0, 1, 3 };
        int[] sol = solution.solution(A, B);
        assertArrayEquals(Arrays.toString(sol), result, sol);
    }

}