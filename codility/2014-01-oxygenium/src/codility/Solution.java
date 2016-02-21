package codility;

import java.util.Deque;
import java.util.LinkedList;

public class Solution {

    /*

     An integer K and a non-empty zero-indexed array A consisting of N integers are given.

     A pair of integers (P, Q), such that 0 ≤ P ≤ Q < N, is called a slice of array A.

     A bounded_slice is a slice in which the difference between the maximum and minimum
     values in the slice is less than or equal to K. More precisely it is a slice, such
     that max(A[P], A[P + 1], ..., A[Q]) − min(A[P], A[P + 1], ..., A[Q]) ≤ K.

     The goal is to calculate the number of bounded_slices.

     For example, consider K = 2 and array A such that:
     A[0] = 3
     A[1] = 5
     A[2] = 7
     A[3] = 6
     A[4] = 3

     There are exactly nine bounded_slices:
     (0, 0), (0, 1), (1, 1), (1, 2), (1, 3), (2, 2), (2, 3), (3, 3), (4, 4).

     Write a function:

     class Solution { public int solution(int K, int[] A); }

     that, given an integer K and a non-empty zero-indexed array A of N integers,
     returns the number of bounded_slices of array A.

     If the number of bounded_slices is greater than 1,000,000,000, the function
     should return 1,000,000,000.

     For example, given:

     A[0] = 3
     A[1] = 5
     A[2] = 7
     A[3] = 6
     A[4] = 3

     the function should return 9, as explained above.
     Assume that:
     N is an integer within the range [1..100,000];
     K is an integer within the range [0..1,000,000,000];
     each element of array A is an integer within the range
     [−1,000,000,000..1,000,000,000].
     Complexity:

     expected worst-case time complexity is O(N);

     expected worst-case space complexity is O(N), beyond input storage (not
     counting the storage required for input arguments).

     Elements of input arrays can be modified.
     */
    private void applyMinimum(int index, Deque<Integer> deque, int[] A) {
        int lastIndex = deque.getLast();
        while (A[lastIndex] > A[index] && !deque.isEmpty()) {
            deque.removeLast();
            if (!deque.isEmpty()) {
                lastIndex = deque.getLast();
            } else {
                break;
            }
        }
        deque.addLast(index);
    }

    private void applyMaximum(int index, Deque<Integer> deque, int[] A) {
        int lastIndex = deque.getLast();
        while (A[lastIndex] < A[index] && !deque.isEmpty()) {
            deque.removeLast();
            if (!deque.isEmpty()) {
                lastIndex = deque.getLast();
            } else {
                break;
            }
        }
        deque.addLast(index);
    }

    private void removeIfBelow(int index, Deque<Integer> deque) {
        int firstIndex = deque.getFirst();
        while (firstIndex < index && !deque.isEmpty()) {
            deque.removeFirst();
            firstIndex = deque.getFirst();
        }
    }

    private int getExtreme(Deque<Integer> deque) {
        return deque.getFirst();
    }

    public int solution(int K, int[] A) {
        int ahead = 1;
        Deque<Integer> maxIndexDeque = new LinkedList<Integer>();
        Deque<Integer> minIndexDeque = new LinkedList<Integer>();

        maxIndexDeque.add(0);
        minIndexDeque.add(0);

        int count = 0;
        for (int i = 0; i < A.length; i++) {
            removeIfBelow(i, minIndexDeque);
            removeIfBelow(i, maxIndexDeque);

            int minIndex = getExtreme(minIndexDeque);
            int maxIndex = getExtreme(maxIndexDeque);

            while ((A[maxIndex] - A[minIndex] <= K) && (ahead < A.length) && (ahead >= i)) {
                applyMaximum(ahead, maxIndexDeque, A);
                applyMinimum(ahead, minIndexDeque, A);

                minIndex = getExtreme(minIndexDeque);
                maxIndex = getExtreme(maxIndexDeque);

                if (A[maxIndex] - A[minIndex] <= K && ahead < A.length) {
                    ahead++;
                }
            }
            if (ahead <= i) {
                ahead = i + 1;
                count++;
                if (count > 1000000000) {
                    return 1000000000;
                }
            } else {
                count += ahead - i;
                if (count > 1000000000) {
                    return 1000000000;
                }
            }
        }

        return count;
    }

}
