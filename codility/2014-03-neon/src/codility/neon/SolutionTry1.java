package codility.neon;

import java.util.Arrays;

public class SolutionTry1 {
    public enum Direction {
        LEFT, RIGHT, OK
    }

    int getBoatOverlap(int[] array, int X, int M, int boatIndex) {
        int minValue;
        int maxValue;

        // Molo borders
        minValue = array[boatIndex];
        maxValue = X;
        if (minValue < maxValue) {
            return minValue - maxValue;
        }

        minValue = array[boatIndex];
        maxValue = (M - 1) - X;
        if (minValue > maxValue) {
            return minValue - maxValue;
        }

        // Boat neighbours
        minValue = array[boatIndex - 1] + X;
        maxValue = array[boatIndex] - X;
        if ((boatIndex - 1 >= 0) && minValue > maxValue) {
            return maxValue - minValue;
        }
        minValue = array[boatIndex + 1] - X;
        maxValue = array[boatIndex] + X;
        if ((boatIndex + 1 < array.length) && minValue < maxValue) {
            return maxValue - minValue;
        }

        return 0;
    }

    boolean checkAllBoats(int []array, int X, int M) {
        for (int boatIndex = 0; boatIndex < array.length; boatIndex++) {
            if (getBoatOverlap(array, X, M, boatIndex) != 0) {
                return false;
            }
        }
        return true;
    }

    boolean isInBounds(int rightExcluding, int index) {
        return index >= 0 && index < rightExcluding;
    }

    public boolean canMoveBoatDirectly(int[] array, int X, int M, int boatIndex, int diff) {
        if (!isInBounds(array.length, boatIndex)) {
            return false;
        }
        int oldBoat = array[boatIndex];
        array[boatIndex] = oldBoat + diff;
        boolean result = false;
        if (getBoatOverlap(array, X, M, boatIndex) == 0) {
            result = true;
        }
        array[boatIndex] = oldBoat;
        return result;
    }

    public boolean moveBoatIfCan(int[] array, int X, int M, int boatIndex, int diff) {
        int tmpBoatIndex = boatIndex;

        while (isInBounds(M, tmpBoatIndex) && !canMoveBoatDirectly(array, X, M, tmpBoatIndex, diff)) {
            tmpBoatIndex += diff;
        }

        if (isInBounds(M, tmpBoatIndex)) {
            // we have found true
            do {
                array[tmpBoatIndex] += diff;
                tmpBoatIndex -= diff;
            } while (tmpBoatIndex + diff != boatIndex);
            return true;
        }
        return false;
    }

    public int solution(int[] R, int X, int M) {
        if (M < 2 * X * R.length) {
            return -1;
        }

        int[] tmp = new int[R.length];

        // initialization
        System.arraycopy(R, 0, tmp, 0, R.length);

        boolean worthTrying = true;
        while (worthTrying && !checkAllBoats(tmp, X, M)) {
            worthTrying = false;
            for (int boatIndex = 0; boatIndex < tmp.length; boatIndex++) {
                int diff = getBoatOverlap(tmp, X, M, boatIndex);
                worthTrying |= moveBoatIfCan(tmp, X, M, boatIndex, diff);
                if (worthTrying) {
                    break;
                }
            }
        }


        System.out.println(Arrays.toString(tmp));
        // compute maxDifference
        int max = 0;
        for (int i = 0; i < R.length; i++) {
            int diff = Math.abs(R[i] - tmp[i]);
            if (diff > max) {
                max = diff;
            }
        }

        return max;
    }

}
