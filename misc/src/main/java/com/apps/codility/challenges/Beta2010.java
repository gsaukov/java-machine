package com.apps.codility.challenges;

/*
    We draw N discs on a plane. The discs are numbered from 0 to N − 1. An array A of N non-negative integers, specifying the radiuses of the discs, is given. The J-th disc is drawn with its center at (J, 0) and radius A[J].

    We say that the J-th disc and K-th disc intersect if J ≠ K and the J-th and K-th discs have at least one common point (assuming that the discs contain their borders).

    The figure below shows discs drawn for N = 6 and A as follows:

      A[0] = 1
      A[1] = 5
      A[2] = 2
      A[3] = 1
      A[4] = 4
      A[5] = 0

    There are eleven (unordered) pairs of discs that intersect, namely:

    discs 1 and 4 intersect, and both intersect with all the other discs;
    disc 2 also intersects with discs 0 and 3.
    Write a function:

    class Solution { public int solution(int[] A); }

    that, given an array A describing N discs as explained above, returns the number of (unordered) pairs of intersecting discs. The function should return −1 if the number of intersecting pairs exceeds 10,000,000.

    Given array A shown above, the function should return 11, as explained above.

    Write an efficient algorithm for the following assumptions:

    N is an integer within the range [0..100,000];
    each element of array A is an integer within the range [0..2,147,483,647].
 */


public class Beta2010 {

    public static void main(String[] args) {
        int[] A = new int[]{1, 5, 2, 1, 4, 0};
        System.out.println(solution(A));
    }

    public static int solution(int[] A) {

        int N = A.length;
        int[] sum = new int[N];
        // count the rightmost point for each circle;
        for (int i = 0; i < N; i++) {
            int right;
            if (N - 1 >= A[i] + i) {
                right = i + A[i];
            } else {
                right = N - 1;
            }
            sum[right]++;
        }
        //  summing up since `i` cannot be reached/intersected from the left, there is no way the `right-er` can;
        for (int i = 1; i < N; i++) {
            sum[i] += sum[i - 1];
        }
        // get the total amount of combinations;
        int result = N * (N - 1) / 2;
        for (int j = 0; j < N; j++) {
            int left; // the leftmost point the current circle can reach;
            if (j - A[j] < 0) { // avoid invalid;
                left = 0;
            } else {
                left = j - A[j];
            }
            if (left > 0) { // if it's valid, sum[left-1] will be the un-intersected, subtract it;
                result -= sum[left - 1];
            }
        }

        return result;
    }
}
