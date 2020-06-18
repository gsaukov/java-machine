package com.apps.hackerrank;

import java.util.Arrays;

public class ArrayManipulation2 {

    static long arrayManipulation(int n, int[][] queries) {
        int size = n;

        if(queries[0].length == 2){
            size = queries[0][0];
            queries = Arrays.copyOfRange(queries, 1, queries.length);
        }

        size = size + 1;

        long[] A = new long[size];
        P[] m = mergeQueries(size, queries);

        long max = applyQuery(A, m);

        return max;
    }

    private static long applyQuery(long[] a, P[] m) {
        long val = 0;
        long max = Long.MIN_VALUE;
        for(int i=0; i<a.length; i++){
            if(m[i]!=null){
                val = addVal(val, m[i]);
                a[i] = val;
                max = Math.max(max, val);
                val = removeVal(val, m[i]);
            }
        }
        return max;
    }

    private static long addVal(long val, P p) {
        val += p.start;
        return val;
    }

    private static long removeVal(long val, P p) {
        val -= p.end;
        return val;
    }

    static P[] mergeQueries(int size, int[][] queries) {
        P[] m = new P[size];

        for(int i = 0; i<queries.length; i++){
            int start = queries[i][0];
            int end = queries[i][1];
            int num = queries[i][2];

            addP(m, start, num, true);
            addP(m, end, num, false);
        }

        return m;
    }

    public static class P {
        int start;
        int end;

        public P(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    static void addP(P[] m, int pos, int value, boolean isStart) {
//        System.out.println(q + " pos " + pos + " val "+ value + " start "+ isStart);
        P p;
        if(m[pos] == null){
            p = new P(0,0);
            m[pos] = p;
        } else {
            p = m[pos];
        }

        if(isStart){
            p.start += value;
        } else {
            p.end += value;
        }
    }

    public static void main(String[] args) {

        int[][] A = new int[][]{{1, 5, 3},{4, 8, 7}, {6, 9, 1},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7}};
        System.out.println(arrayManipulation(10, A));
    }



}
