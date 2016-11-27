package tsp;

import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * TSP solver by mapping 2D to 1D based on center of gravity.
 *
 * @author vbmacher
 */
public abstract class TSP {
    protected final List<Point> points = new LinkedList<>();

    public int size() {
        return points.size();
    }

    public Point[] getPoints() {
        return points.toArray(new Point[0]);
    }

    public boolean add(Point e) {
        return points.add(e);
    }

    public boolean remove(Object o) {
        return points.remove(o);
    }

    public void clear() {
        points.clear();
    }

    public Point get(int index) {
        return points.get(index);
    }

    public Point remove(int index) {
        return points.remove(index);
    }

    public Point getCenterOfGravity() {
        if (points.isEmpty()) {
            return null;
        }

        Point centerOfGravity = new Point();
        for (Point point : points) {
            centerOfGravity.x += point.x;
            centerOfGravity.y += point.y;
        }
        centerOfGravity.x /= points.size();
        centerOfGravity.y /= points.size();

        return centerOfGravity;
    }

    public double getPathLength(Point[] solution) {
        double len = 0;
        for (int i = 1; i < solution.length; i++) {
            len += Point.distance(solution[i - 1].x, solution[i - 1].y, solution[i].x, solution[i].y);
        }
        return len;
    }

    public abstract Map<Double,Point> solveWithDistances(Point centerOfGravity);

    public Map<Double,Point> solveWithDistances() {
        Point cog = getCenterOfGravity();
        if (cog == null) {
            return null;
        }
        return solveWithDistances(cog);
    }

    public Point[] solve() {
        return solveWithDistances().values().toArray(new Point[0]);
    }

    @Override
    public String toString() {
        return "TSP{" + "points=" + Arrays.toString(points.toArray()) + '}';
    }

}
