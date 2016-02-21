package fluorum;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SolutionTest {
    private Solution solution;

    private int K;
    private int[] T;

    private int[] expected;

    @Before
    public void setUp() {
        solution = new Solution();
    }

    @After
    public void tearDown() {
        int[] result = solution.solution(K, T);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void test01() {
        K = 2;
        T = new int[] { 1,2,3,3,2,1,4 };
        expected = new int[] { 2, 0, 6, 3, 5 };
    }

    @Test
    public void test02() {
        K = 3;
        T = new int[] { 1,3,2,2,3,3,4,4,7,7,11,8 };
        expected = new int[] { 3,10,0,2,5,6,9 };
    }

//    2:[1, 2, 3, 3, 2, 1, 4]; expected [2, 0, 6, 3, 5]
    // 0:[0]; expected [0]
    // 0:[0, 0];  expected [0, 1]
    // 0:[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 99]; expected [0, 99]
    // 2:[1, 2, 2, 2, 2, 4, 2, 5, 4, 8, 1]; expected [2, 7, 0, 9, 3, 6, 1..
    // 2:[4, 2, 2, 4, 2, 4, 2, 2]; expected [2, 0, 1, 3, 5, 6, 7..
    // 7:[6, 7, 7, 6, 3, 1, 2, 7]; expected [7, 4, 5, 0]
    //
}
