package test;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static java.util.stream.Collectors.toList;

public class FindPath {
    private final static int OBSTACLE = -1;
    private final int[][] map; // N x N

    @SuppressWarnings("unchecked")
    private FindPath(List<List<Integer>> map) {
        List<Integer>[] tmp = map.toArray(new List[map.size()]);
        this.map = new int[tmp.length][tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp.length; j++) {
                this.map[i][j] = tmp[i].get(j);
            }
        }
    }

    public static FindPath fromMap(String map) {
        List<List<Integer>> parsedMap = Arrays.stream(map.split("\n")).map(String::toCharArray).map(chars -> {
            List<Integer> row = new ArrayList<>();
            for (char c : chars) {
                row.add(c == ' ' ? 0 : OBSTACLE);
            }
            return row;
        }).collect(toList());

        return new FindPath(parsedMap);
    }

    private int[][] initDistances() {
        int[][] distances = new int[map.length][map.length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                distances[i][j] = Integer.MAX_VALUE;
            }
        }

        return distances;
    }

    /**
     * Find minimal distance between starting and goal position.
     *
     * @param start - start position on the map
     * @param stop - goal position on the map
     * @return -1 if there is no way; or minimal steps to reach stop position from start position
     */
    public int minDistance(Point start, Point stop) {
        Queue<Point> toDiscover = new LinkedList<>();
        int[][] distances = initDistances();

        if (map[start.y][start.x] == OBSTACLE) {
            return -1;
        }

        distances[start.y][start.x] = 0;
        toDiscover.add(start);

        // TODO: FIND MINIMAL DISTANCE FROM start TO stop USING ONLY THE FOLLOWING:
        // - toDiscover
        // - distances
        // - canGo()
        // - left(), right(), up(), down()
        // - isWorthGoing()


        return -1;
    }

    private boolean isWorthGoing(int[][] distances, Point where, Point currentPoint) {
        return distances[where.y][where.x] > (distances[currentPoint.y][currentPoint.x]) + 1;
    }

    private boolean canGo(Point point) {
        int x = point.x;
        int y = point.y;
        return !(x < 0 || y < 0 || x >= map.length || y >= map.length) && map[y][x] != OBSTACLE;
    }

    private Point left(Point point) {
        return new Point(point.x - 1, point.y);
    }

    private Point right(Point point) {
        return new Point(point.x + 1, point.y);
    }

    private Point up(Point point) {
        return new Point(point.x, point.y - 1);
    }

    private Point down(Point point) {
        return new Point(point.x, point.y + 1);
    }

    public String toString(Point start, Point stop) {
        String str = "";
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                int val = map[i][j];
                String add = (val == 0 ? "." : "*");

                if (i == start.y && j == start.y) {
                    add = "O";
                } else if (i == stop.y && j == stop.x) {
                    add = "X";
                }
                str += add;

            }
            str += "\n";
        }
        return str;
    }
}
