package tsp;

import java.awt.Point;
import java.util.SortedMap;
import java.util.TreeMap;

public class TSPDistances extends TSP {

    @Override
    public SortedMap<Double, Point> solveWithDistances(Point cog) {
        // Map 2D to 1D
        SortedMap<Double, Point> path = new TreeMap<>();
        for (Point point : points) {
            double dist = Point.distance(cog.x, cog.y, point.x, point.y);
            path.put(dist, point);
        }

        return path;
    }

}
