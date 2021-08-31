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
        int totalIntersecions = 0;
        // J ≠ K so first array element radius will be reduced on one.
        totalIntersecions = getIntersectionsNumberForPoint(Math.max(0, A[0] - 1), 0, A);
        for (int i = 1; i < A.length; i++) { //start with 2nd element since 1st already calculated.
            if (totalIntersecions > 10000000) {
                return -1;
            }
            totalIntersecions += getIntersectionsNumberForPoint(A[i], i, A);
        }

        return totalIntersecions;
    }

    public static int getIntersectionsNumberForPoint(int radius, int center, int[] A) {
        int start = Math.max(0, center - radius); // positive Left
        int end = Math.min(A.length - 1, center + radius); // bounded right
//        for(int i = start; i < end; i++) {
//            if(A[i] )
//        }
        int len = Math.max(0, (end - start) - 1); // -1 not counting self.
        return len;
    }

}
