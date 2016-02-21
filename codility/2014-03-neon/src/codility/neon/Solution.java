package codility.neon;

/*

 You are helping the port management to organize boats moored in a port. There is a straight wharf with N mooring
 bollards and N boats. The wharf (and the dock in front of it) is of length M. Each boat has the same width: 2*X.
 The bollards are located at the very edge of the wharf. It is possible for more than one bollard to be at the same
 position.

 You have to moor each boat to a separate bollard so that the following rules are satisfied:

 - each boat is fixed with a single mooring rope to the bank of the wharf,
 - the mooring rope connects the middle of the boat's bow with a bollard,
 - the sides of the boats can touch each other,
 - boats cannot overlap,
 - boats cannot be placed outside the dock or extend it,
 - two boats cannot be tied to the same bollard.

 All the boats must have mooring ropes of the same length. The goal is to minimize this length.

 More formally, let the max_distance be the largest distance between the middle of any boat's bow and the bollard to
 which the boat is moored. The goal is to align the boats so that the max_distance is as small as possible. You are
 given a non-empty zero-indexed array R of N integers, and two positive integers X and M. Array R contains the positions
 of the bollards along the wharf. The wharf's ends are at positions 0 and M.

 For example, the following array R, and integers X and M:
 R[0] = 1    X = 2
 R[1] = 3    M = 16
 R[2] = 14

 describe:

 - three bollards at positions 1, 3 and 14,
 - three boats of width 4,
 - a wharf of length 16.

 You can set:

 - the center of the first boat at position 2,
 - the center of the second boat at position 6,
 - the center of the third boat at position 14.

 Between the first boat and the first ring the distance is 1; between the second boat and the second ring it is 3; and
 between the third boat and the third ring it is 0; so the max_distance is 3.

 Write a function:

 class Solution2 { public int solution(int[] R, int X, int M); }

 that, given a zero-indexed array R consisting of N integers, given two integers X and M, returns the minimal
 max_distance you can achieve.

 If it is not possible to tie all the boats, the function should return −1.

 For example, given the following array R, integers X and M:

 R[0] = 1    X = 2
 R[1] = 3    M = 16
 R[2] = 14

 the function should return 3, as explained above.

 Assume that:

 N is an integer within the range [1..100,000];
 X and M are integers within the range [1..1,000,000,000];
 each element of array R is an integer within the range [0..M];
 array R is sorted in non-decreasing order.

 Complexity:
 - expected worst-case time complexity is O(N);
 - expected worst-case space complexity is O(N), beyond input storage (not counting the storage required for input arguments).

 Elements of input arrays can be modified.

 */
public class Solution {

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

    public int[] getMaxDist(int[] dist, int from, int to) {
        int max = 0;
        int origMax = 0;
        for (int j = from; j <= to; j++) {
            if (Math.abs(dist[j]) > max) {
                max = Math.abs(dist[j]);
                origMax = dist[j];
            }
        }
        return new int[]{max, origMax};
    }

    public int[] findMinims(int[] B) {
        int[] minims = new int[B.length];

        int actualMinimum = Integer.MAX_VALUE;
        for (int i = B.length - 1; i >= 0; i--) {
            if (B[i] < actualMinimum) {
                actualMinimum = B[i];
            }
            minims[i] = actualMinimum;
        }
        return minims;
    }

    public int solution(int[] R, int X, int M) {
        if (M < (2 * X * R.length) || (R.length == 0)) {
            return -1;
        }

        int[] boats = getBoats(R.length, X);
        int[] dist = getDist(R, boats);

        int maxAllowedPosition = M - X;
        if (boats[boats.length - 1] < maxAllowedPosition) {

            // those on the left - let's jump over, because they cannot improve their position
            int i = 0;
            while (i < dist.length && dist[i] <= 0) {
                i++;
            }
            int[] maxDistLeft = getMaxDist(dist, 0, i - 1);
            int[] maxDistRight = getMaxDist(dist, i, R.length - 1);

            // if on the left the max distance is greater or equal to that on the right, it impossible to change it anyway.
            if (maxDistLeft[0] >= maxDistRight[0]) {
                return maxDistLeft[0];
            }
            // if the maximum is negative, it's impossible to change it - boats cannot be moved to the left
            if (maxDistRight[1] < 0) {
                return maxDistRight[0];
            }


            int[] mins = findMinims(dist);
      //      System.out.println(Arrays.toString(mins));

            int moving = 0;
            for (int j = 0; j < R.length; j++) {
                int can = (dist[j] - moving + (mins[j] - moving)) / 2;
                if (can > 0) {
                    if (boats[R.length - 1] + moving + can <= maxAllowedPosition) {
                        moving += can;
                    } else {
                        can = maxAllowedPosition - (boats[R.length - 1] + moving);
                        if (can > 0) {
                            moving += can;
                        }
                    }
                }
                boats[j] += moving;
            }
            dist = getDist(R, boats);
        }

//        System.out.println("......................");
//        System.out.println(Arrays.toString(boats));
//        System.out.println(Arrays.toString(dist));

        // finally get max dist
        return getMaxDist(dist);
    }
}
