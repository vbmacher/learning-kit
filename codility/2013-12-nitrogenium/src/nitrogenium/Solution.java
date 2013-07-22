package nitrogenium;

public class Solution {

  public int[] solution(int[] A, int[] B) {
    int[] result = new int[B.length];

    int maxA=0;
    for (int i = 0; i < A.length; i++) {
      if (A[i] > maxA) {
        maxA = A[i];
      }
    }

    int[] islands = new int[maxA+1];

    int i = 0;
    while (A[i] == 0) {
      i++;
      if (i >= A.length) {
        return result;
      }
    }
    if (A[i] > 0) {
      islands[0] = 1;
    }
    boolean climbing = true;
    for (; i < A.length-1; i++) {
      if (!climbing && (A[i] < A[i + 1])) {
        islands[A[i]]++;
        climbing = true;
      } else if (climbing && (A[i] > A[i + 1])) {
        islands[A[i]]--;
        climbing = false;
      }
    }
    if (climbing && i > 0) {
      islands[A[i]]--;
    }

    int count = 0;
    for (i = 0; i < maxA; i++) {
      count += islands[i];
      islands[i] = count;
    }

    for (i = 0; i < B.length; i++) {
      if (B[i] >= maxA) {
        continue;
      }
      result[i] = islands[B[i]];
    }

    return result;
  }

}
