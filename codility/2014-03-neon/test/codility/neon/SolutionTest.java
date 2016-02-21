package codility.neon;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SolutionTest {

    private int[] R = null;
    private int X = 0;
    private int M = 0;
    private int expected;
    private Solution solution;

    @Before
    public void setUp() {
        solution = new Solution();
    }

    @After
    public void tearDown() {
        assertEquals(expected, solution.solution(R, X, M));
    }

    @Test
    public void test01() {
        R = new int[] { 1, 3, 14 };
        X = 2;
        M = 16;
        expected = 3;
    }

    @Test
    public void test02() {
        R = new int[] { 50 };
        X = 1000000000;
        M = 1000000000;
        expected = -1;
    }

    @Test
    public void test03() {
        R = new int[] { 0 };
        X = 1;
        M = 1;
        expected = -1;
    }

    @Test
    public void test04() {
        R = new int[] { 99, 99 };
        X = 45;
        M = 195;
        expected = 45;
    }

    @Test
    public void test05() {
        R = new int[] { 4 };
        X = 50;
        M = 99;
        expected = -1;
    }

    @Test
    public void test06() {
        R = new int[] { 0, 4, 5, 6 };
        X = 1;
        M = 20;
        expected = 1;
    }

    @Test
    public void test07() {
        R = new int[] { 5, 8, 74, 84, 95, 96 };
        X = 7;
        M = 100;
        expected = 23;
    }

    @Test
    public void test08() {
        R = new int[] { 4255, 4880, 5113, 7931, 8072 };
        X = 800;
        M = 100000;
        expected = 1292;
    }

    @Test
    public void test09() {
        R = new int[] { 50938, 50955, 51107, 52260, 53044, 53305, 53387, 53686, 54704, 56729, 57137, 57184, 59283, 59411, 60940, 61316, 61391, 61596, 61675, 61967 };
        X = 91;
        M = 62000;
        expected = 135;
    }

    @Test
    public void test10() {
        R = new int[] { 565, 848, 7360, 8355, 9479, 9561 };
        X = 700;
        M = 10000;
        expected = 2260;
    }

    @Test
    public void test11() {
        R = new int[] { 4, 7, 9, 11, 14, 21, 21, 22, 22, 23 };
        X = 1;
        M = 37;
        expected = 3;
    }

    @Test
    public void test12() {
        R = new int[] { 4, 6, 16, 16, 25 };
        X = 2;
        M = 100;
        expected = 2;
    }

    @Test
    public void test13() {
        R = new int[] { 8, 13, 19, 21, 27, 38, 82, 88 };
        X = 3;
        M = 100;
        expected = 3;
    }

    @Test
    public void test14() {
        R = new int[] { 258916750, 404934137, 420571581, 511274721, 757954403, 844421852 };
        X = 80000000;
        M = 1000000000;
        expected = 138916750;
    }

    @Test
    public void test15() {
        R = new int[] { 0 };
        X = 20;
        M = 100;
        expected = 20;
    }

}
