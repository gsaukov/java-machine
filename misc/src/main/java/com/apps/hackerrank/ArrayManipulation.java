package com.apps.hackerrank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayManipulation {

    static long arrayManipulation(int n, int[][] queries) {
        int size = n;

        if(queries[0].length == 2){
            size = queries[0][0];
            queries = Arrays.copyOfRange(queries, 1, queries.length);
        }

        long[] A = new long[size + 1];
        for(int[] query : queries) {
            applyQuery(A, query);
        }

        long max = Long.MIN_VALUE;
        for(long e : A){
            max = Math.max(max, e);
        }

        return max;
    }

    static void applyQuery(long[] A, int[] query) {
        int start = query[0];
        int end = query[1];
        int num = query[2];

        for (int i = start; i <= end; i++){
            A[i] += num;
        }
    }

    public static void main(String[] args) {

        int[][] A = new int[][]{{1, 5, 3},{4, 8, 7}, {6, 9, 1},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7}};
//        int[][] A = new int[][]{{5, 3}, {1, 2, 100}, {2, 5, 100}, {3, 4, 100}};
        for(int i = 0; i<1000000; i++){
            arrayManipulation(100, A);
        }
        System.out.println(arrayManipulation(10, A));
    }



}
