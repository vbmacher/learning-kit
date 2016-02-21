package tsp;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TSPConvex extends TSP {

    @Override
    public Map<Double, Point> solveWithDistances(Point cog) {
        // based on sorted angles between lines (cog, point)
        Map<Double, Point> path = new LinkedHashMap<>();

        if (points.size() < 2) {
            path.put(new Double(0), points.get(0));
            return path;
        }

        int i = 0, size = points.size();
        List<Point> disjunctPoints = new ArrayList<>(points);
        disjunctPoints.remove(0);
        path.put(new Double(0), points.get(0));
        System.out.println("----");

        do {
            System.out.println("for " + i + "=[" + points.get(i).x + "," + points.get(i).y + "]");
            Point current = points.get(i);
            int minJ = i+1;
            double minAngle = Double.MAX_VALUE;
            for (Point p2 : disjunctPoints) {
                int vec1x = cog.x + current.x;
                int vec1y = cog.y + current.y;
                int vec2x = cog.x + p2.x;
                int vec2y = cog.y + p2.y;
                double cosAngle =
                        (vec1x * vec2x + vec1y * vec2y)
                        / (Math.hypot(vec1x, vec1y) * Math.hypot(vec2x, vec2y));
                double angle = Math.toDegrees(Math.acos(cosAngle));
                System.out.println("\tcurrent=[" + current.x + "," + current.y + "] p=[" + p2.x + "," + p2.y + "] angle=" + angle);
                if (angle < minAngle) {
                    minAngle = angle;
                    minJ = points.indexOf(p2);
                }
            }
            System.out.print("cog=[" + cog.x + "," + cog.y + "] minJ=" + minJ + " minAngle=" + minAngle);

            if (minJ >= 0 && minJ < size) {
                i = minJ;
                Point p = points.get(i);
                path.put(minAngle, p);
                disjunctPoints.remove(p);
                System.out.println(" min=[" + p.x + "," + p.y + "] " + disjunctPoints.size());
            } else {
                i++;
                System.out.println(" X");
                if (i < size) {
                    disjunctPoints.remove(points.get(i));
                }
            }
        } while (i < size && !disjunctPoints.isEmpty());
System.out.println(path.size() + " / " + points.size());



        return path;
    }

}
