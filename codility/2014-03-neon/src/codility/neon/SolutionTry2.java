package codility.neon;

import java.util.Arrays;

public class SolutionTry2 {
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


    public int solution(int[] R, int X, int M) {
        if (M < 2 * X * R.length) {
            return -1;
        }
        int[] boats = getBoats(R.length, X);
        int[] dist = getDist(R, boats);

        System.out.println(Arrays.toString(boats));
        System.out.println(Arrays.toString(dist));

        int maxAllowedPosition = M - X;
        int minAllowedPosition = maxAllowedPosition;
        if (boats[boats.length - 1] < maxAllowedPosition) {

            // tie ktore su vlavo - preskocit, pretoze oni uz nemozu zlepsit svoju poziciu
            int i = 0;
            while (dist[i] <= 0) {
                i++;
            }
            int maxDistLeft = getMaxDist(dist, 0, i - 1);
            int maxDistRight = getMaxDist(dist, i, R.length - 1);

            // ak nalavo je max distance vacsia alebo rovna ako napravo, uz s nou nemozem robit nic.
            if (maxDistLeft < maxDistRight) {
                // budem pracovat len s tymi, ktori mozu zlepsit svoju poziciu.

                // najprv skusim posunut "cele doprava"
                int minD = dist[i];
                int maxD = dist[boats.length - 1];
                int diff = (maxD + minD) / 2;

                if (diff > 0) {
                    // skontrolujem pravu hranicu
                    if (maxAllowedPosition < boats[boats.length - 1] + diff) {
                        diff = maxAllowedPosition - boats[boats.length - 1];
                    }
                    minAllowedPosition = boats[i];
                    for (int j = i; j < boats.length; j++) {
                        boats[j] += diff;
                        dist[j] -= diff;
                    }
                }

                for (int j = boats.length - 1; j >= i; j--) {
                    int min = dist[j];
                    int max = min;

                    int from = j;
                    int to = j;
                    while (j-1 >= i && dist[j] < dist[j - 1]) {
                        max = dist[j-1];
                        j--;
                        from = j;
                    }
                    // mozem ich posunut o:
                    int can = (max + min) / 2;
                    System.out.println(min + "-" + max + " can=" + can + ", max=" + maxAllowedPosition);

                    // kontrola hranice
                    if (maxAllowedPosition < boats[to] + can) {
                        can = maxAllowedPosition - boats[to];
                        System.out.println("Reducing can=" + can);

                        // TODO: musim ist z opacnej strany
//                    } else if (minAllowedPosition > boats[from] + can) {
//                        can = minAllowedPosition - boats[from];
//                        System.out.println("Reducing can=" + can);
                    }

                    maxAllowedPosition = (boats[from] + can - 2*X);

                    if (can > 0) {
                        for (int k = from; k <= to; k++) {
                            boats[k] += can;
                            dist[k] -= can;
                        }
                    }
                    System.out.println("From = " + boats[from] + " to = " + boats[to]);
                }
            }

        }

        // konecne urcenie vitaza
        return getMaxDist(dist);
    }

}
