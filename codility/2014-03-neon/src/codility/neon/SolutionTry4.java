package codility.neon;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

public class SolutionTry4 {

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

    public int solution(int[] R, int X, int M) {
        if (M < 2 * X * R.length) {
            return -1;
        }
        if (2 * X > M) {
            return -1;
        }
        if (R.length == 0) {
            return 0;
        }
        System.out.println(Arrays.toString(R) + ", X=" + X + ", M=" + M);

        int[] diff = new int[R.length];

        diff[0] = R[0];
        for (int i = 1; i < R.length; i++) {
            diff[i] = R[i] - R[i-1];
        }

        int[] gaps = new int[R.length];
        gaps[0] = diff[0] - X;

        for (int i = 1; i < R.length; i++) {
            gaps[i] = diff[i] - 2*X;
        }
        System.arraycopy(R, 0, diff, 0, R.length);

       // System.out.println(Arrays.toString(gaps));

       // System.out.println("-----------------------------------------");


        int howMuchIamAdding = 0;
        Deque<Integer> leftHighPositions = new LinkedList<>();
        for (int i = 0; i < R.length; i++) {
            diff[i] += howMuchIamAdding;
      //      System.out.println("diff[" + i + "]=" + diff[i] + ", howMuch="
        //            + howMuchIamAdding + ", gaps[" + i + "]=" + gaps[i]);

            if (gaps[i] > 0) {
                gaps[i] -= howMuchIamAdding;
                if (gaps[i] > 0) {
                    leftHighPositions.add(i);
                    howMuchIamAdding = 0;
                }
            }

            if (gaps[i] < 0) {
                // musim sa rozdelit - prva polovica vlavo vybavim hned
                // druhu polovicu vpravo doplnit do howMuchIamAdding
                int toTheLeft = (int)Math.ceil((double)-gaps[i] / 2.0);
                int toTheRight = -gaps[i] / 2;
                howMuchIamAdding += toTheRight;

                int remainder;
                if (!leftHighPositions.isEmpty()) {
                    int position = leftHighPositions.removeLast();
                    int oldPosition = i;

                    remainder = gaps[position] - toTheLeft;
              //      System.out.println("Next gap[" + position + "]=" + gaps[position] + ", remainder=" + remainder);
                    while (remainder < 0 && !leftHighPositions.isEmpty()) {
                        oldPosition = position;
                        position = leftHighPositions.removeLast();
                        for (int j = position; j < oldPosition; j++) {
                            diff[j] -= gaps[oldPosition];
                        }
                        gaps[oldPosition] = 0;
                        remainder = gaps[position] - remainder;
                    }
                    if (remainder > 0) {
                        for (int j = position; j < oldPosition; j++) {
                            diff[j] -= (gaps[position] - remainder);
         //                   System.out.println("    diff[" + j + "] = " + diff[j]);
                        }
                        diff[i] += toTheRight;
                        gaps[i] += toTheRight;
                        gaps[position] = remainder;
                        gaps[oldPosition] += toTheLeft;
                        leftHighPositions.add(position);
                    } else {
                        gaps[position] = 0;
                        howMuchIamAdding += -remainder;
                    }
                } else {
                    howMuchIamAdding += toTheLeft;
                    diff[i] += toTheLeft;
                }
            }
        }

        System.out.println(Arrays.toString(diff));
       // System.out.println(Arrays.toString(gaps));

        System.out.println("-----------------------------------------");

        int[] dist = getDist(R, diff);

        // [4, 7, 9, 11, 14, 21, 21, 22, 22, 23], X=1, M=37
        // [4, 7, 9, 11, 14, 17, 19, 21, 23, 25]


        return getMaxDist(dist);
    }
}
