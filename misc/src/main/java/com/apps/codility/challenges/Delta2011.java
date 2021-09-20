package com.apps.codility.challenges;

/*
For a given array A of N integers and a sequence S of N integers from the set {−1, 1}, we define val(A, S) as follows:

val(A, S) = |sum{ A[i]*S[i] for i = 0..N−1 }|

(Assume that the sum of zero elements equals zero.)

For a given array A, we are looking for such a sequence S that minimizes val(A,S).

Write a function:

class Solution { public int solution(int[] A); }

that, given an array A of N integers, computes the minimum value of val(A,S) from all possible values of val(A,S) for all possible sequences S of N integers from the set {−1, 1}.

For example, given array:

  A[0] =  1
  A[1] =  5
  A[2] =  2
  A[3] = -2
your function should return 0, since for S = [−1, 1, −1, 1], val(A, S) = 0, which is the minimum possible value.

Write an efficient algorithm for the following assumptions:

N is an integer within the range [0..20,000];
each element of array A is an integer within the range [−100..100].

 */


import java.util.Arrays;

public class Delta2011 {

    public static void main(String[] args) {
        int[] A = new int[]{1, 5, 2, 1, 4, 0};
        System.out.println(solution(A));
    }

    public static int solution(int[] A) {

        int totalSum = 0;
        int max = 0;

        for (int i = 0; i < A.length; i++) {
            A[i] = Math.abs(A[i]);
            max = Math.max(max, A[i]);
            totalSum += A[i];
        }

        if(max > totalSum / 2) {
            return totalSum - max;
        }

        int currentSum = 0;
        int j = A.length - 1;
        int minSum = Integer.MAX_VALUE;
        return 0;

    }

}
