package com.apps.hackerrank.DaysOfCode;

import java.util.Arrays;

public class SortingAlgorithms {

    public static void main(String[] args) {
        int [] A = new int[]{0, 1, 2, 5, 6, 3, 5, 3, 6, 5, 2, 1};
        bubbleSort(A);
        System.out.println(Arrays.toString(A));
    }

    public static void bubbleSort(int[] A) {
        for (int i = 0; i < A.length - 1; i++) {
            for (int j = i + 1; j < A.length; j++) {
                if (A[i] > A[j]) {
                    int t = A[i];
                    A[i] = A[j];
                    A[j] = t;
                }
            }
        }
    }

}
