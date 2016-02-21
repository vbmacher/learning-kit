package codility;

public class Solution {
    private char[] input;
    private int length;

    private int[] pref;
    private int[] prod;

    private int[] hist;

    private void prefixes() {
        pref[0] = length;
        hist[length] = 1;
        int g = 0;
        int f = 0; // shall be undefined

        for (int i = 1; i < length; i++) {
            if ((i < g) && (pref[i - f] != g - i)) {
                pref[i] = Math.min(pref[i - f], g - i);
                hist[pref[i]]++;
            } else {
                g = Math.max(g, i);
                f = i;
                while ((g < length) && (input[g] == input[g - f])) {
                    g++;
                }
                pref[i] = g - f;
                hist[pref[i]]++;
            }
        }
    }

    public int solution(String S) {
        input = S.toCharArray();
        length = input.length;

        pref = new int[length];
        hist = new int[length + 1];
        prefixes();

        prod = new int[length + 1];
        int max = length;
        int level = 0;
        for (int i = max; i > 0; i--) {
            prod[i] = (hist[i] + level) * i;
            level += hist[i];
        }

        for (int i = 0; i < length; i++) {
            if (prod[i] > max) {
                max = prod[i];
                if (max >= 1000000000) {
                    return 1000000000;
                }
            }
        }

        return max;
    }

}
