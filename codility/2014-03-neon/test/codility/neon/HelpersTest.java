package codility.neon;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HelpersTest {
    private Solution solution;

    @Before
    public void setUp() {
        solution = new Solution();
    }

    @Test
    public void testInitialBoats() {
        int[] R = new int[] { 99, 99 };
        int[] boats = new int[] { 45, 135 };
        int X = 45;

        int[] result = solution.getBoats(R.length, X);
        Assert.assertArrayEquals(boats, result);
    }

    @Test
    public void testGetDist() {
        int[] R = new int[] { 99, 99 };
        int[] boats = new int[] { 45, 135 };

        int[] dist = new int[] { 54,-36 };

        int[] result = solution.getDist(R, boats);
        Assert.assertArrayEquals(dist, result);
    }

}
