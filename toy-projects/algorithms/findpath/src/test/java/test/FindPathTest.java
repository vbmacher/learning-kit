package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class FindPathTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "      \n      \n      \n      \n      \n      ", new Point(0,0), new Point(5,5), 10 },
                { "***\n***\n***", new Point(0,0), new Point(0,0), -1},
                { "   *  \n   *  \n   *  \n   *  \n      \n      ", new Point(0,0), new Point(5, 0), 13}
        });
    }

    private final String map;
    private final Point start;
    private final Point stop;
    private final int expectedMinDistance;

    public FindPathTest(String map, Point start, Point stop, int expectedMinDistance) {
        this.map = map;
        this.start = start;
        this.stop = stop;
        this.expectedMinDistance = expectedMinDistance;
    }

    @Test
    public void testMinDistance() {
        FindPath path = FindPath.fromMap(map);

        System.out.println("Map: ");
        System.out.println(path.toString(start, stop));

        int dist = path.minDistance(start, stop);
        assertEquals(expectedMinDistance, dist);
    }


}