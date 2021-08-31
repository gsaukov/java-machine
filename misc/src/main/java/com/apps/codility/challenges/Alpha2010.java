package com.apps.codility.challenges;

/*
    A non-empty array A consisting of N integers is given. The first covering prefix of array A is the smallest integer P such that 0≤P<N and such that every value that occurs in array A also occurs in sequence A[0], A[1], ..., A[P].

    For example, the first covering prefix of the following 5−element array A:

      A[0] = 2
      A[1] = 2
      A[2] = 1
      A[3] = 0
      A[4] = 1
    is 3, because sequence [ A[0], A[1], A[2], A[3] ] equal to [2, 2, 1, 0], contains all values that occur in array A.

    Write a function

    class Solution { public int solution(int[] A); }

    that, given a non-empty array A consisting of N integers, returns the first covering prefix of A.

    For example, given array A such that

      A[0] = 2
      A[1] = 2
      A[2] = 1
      A[3] = 0
      A[4] = 1
    the function should return 3, as explained above.

    Write an efficient algorithm for the following assumptions:

    N is an integer within the range [1..1,000,000];
    each element of array A is an integer within the range [0..N−1].
 */

import java.util.HashSet;
import java.util.Set;

public class Alpha2010 {

    public static void main(String[] args) {
        int[] A = new int[]{4, 3, 0, 1, 2, 7, 3, 0};

        System.out.println(solution(A));
    }

    public static int solution(int[] A) {
        Set<Integer> sequence = new HashSet<>();
        int len = A.length;
        if (len > 1000000 || len < 1) throw new IllegalArgumentException();
        for (int e : A) {
            if (e > len) throw new IllegalArgumentException();
            sequence.add(e);
        }

        for (int i = 0; i < len; i++) {
            sequence.remove(A[i]);
            if (sequence.isEmpty()) {
                return i;
            }
        }

        return len;
    }
}
