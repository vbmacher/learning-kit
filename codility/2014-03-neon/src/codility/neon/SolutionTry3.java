package codility.neon;

import java.util.Arrays;

public class SolutionTry3 {

    public int[] getBoats(int len, int X) {
        int[] boats = new int[len];

        int tmp = X;
        for (int j = 0; j < boats.length; j++) {
            boats[j] = tmp;
            tmp += 2 * X;
        }
        return boats;
    }

    public int[] getDist(int[] R, int[] boats) {
        int[] dist = new int[R.length];
        for (int i = 0; i < R.length; i++) {
            dist[i] = R[i] - boats[i];
        }
        return dist;
    }

    public int getMaxDist(int[] dist) {
        int max = 0;
        for (int d : dist) {
            if (Math.abs(d) > max) {
                max = Math.abs(d);
            }
        }
        return max;
    }

    public int getMaxDist(int[] dist, int from, int to) {
        int max = 0;
        for (int j = from; j <= to; j++) {
            if (Math.abs(dist[j]) > max) {
                max = Math.abs(dist[j]);
            }
        }
        return max;
    }

    public int canToTheRight(int position, int X, int M) {
        return position <= (M-X) ? 0 : position - (M - X);
    }

    public int canToTheLeft(int position, int X) {
        return position >= X ? 0 : position - X;
    }

    public int average(int[] dist, int fromIndex, int toIndex) {
        if (fromIndex == toIndex) {
            return dist[fromIndex];
        }
        int avg = 0;
        for (int i = fromIndex; i <= toIndex; i++) {
            avg += dist[i];
        }
        avg = (int)Math.ceil((double)avg / (double)(toIndex - fromIndex + 1));
        return avg;
    }

    public void move(int[] boats, int[] dist, int fromIndex, int toIndex, int X, int M) {
        int moveDiff = average(dist, fromIndex, toIndex);

        System.out.println("original diff " + moveDiff);
        if (moveDiff > 0) {
            moveDiff -= canToTheRight(boats[toIndex] + moveDiff, X, M);
        } else if (moveDiff < 0) {
            moveDiff += canToTheLeft(boats[fromIndex] + moveDiff, X);
        }

        System.out.println("move from " + fromIndex
                + " to " + toIndex + " diff " + moveDiff);

        for (int i = fromIndex; i <= toIndex; i++) {
            boats[i] += moveDiff;
            dist[i] -= moveDiff;
        }
    }

    public int getMax(int[] dist) {
        int max = 0;
        for (int j = 0; j < dist.length; j++) {
            if (Math.abs(dist[j]) > max) {
                max = Math.abs(dist[j]);
            }
        }
        return max;
    }

    public int solution(int[] R, int X, int M) {
        if (M < R.length * 2 * X) {
            return -1;
        }
        System.out.println(Arrays.toString(R));

        int[] boats = getBoats(R.length, X);
        int[] dist = getDist(R, boats);

        System.out.println(Arrays.toString(boats));
        System.out.println(Arrays.toString(dist));
        System.out.println("----------------------------------------------");

        // global move
        move(boats, dist, 0, dist.length - 1, X, M);

        System.out.println(Arrays.toString(boats));
        System.out.println(Arrays.toString(dist));

        int fromIndex;
        int toIndex;

        int i= 0,j=dist.length - 1;
        boolean was = true;
        while (was) {
            was = false;
            // osekame zlava a aj sprava
            fromIndex = i;
            toIndex = fromIndex;
            while (toIndex + 1 < j && dist[toIndex+1] < dist[toIndex]) {
                toIndex++;
                was = true;
            }
            move(boats, dist, fromIndex, toIndex, X, M);
            i = toIndex;

            toIndex = j;
            fromIndex = toIndex;
            while (fromIndex - 1 > i && dist[fromIndex - 1] > dist[fromIndex]) {
                fromIndex--;
                was = true;
            }
            move(boats, dist, fromIndex, toIndex, X, M);
            j = fromIndex;
        }

        System.out.println("----------------------------------------------");
        System.out.println(Arrays.toString(boats));
        System.out.println(Arrays.toString(dist));

//        R = new int[] { 4, 6, 16, 16, 25 };

        return getMax(dist);

    }

}
