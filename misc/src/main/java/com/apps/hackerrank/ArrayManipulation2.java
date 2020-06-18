package com.apps.hackerrank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayManipulation2 {

    static long arrayManipulation(int n, int[][] queries) {
        int size = n;

        if(queries[0].length == 2){
            size = queries[0][0];
            queries = Arrays.copyOfRange(queries, 1, queries.length);
        }

        size = size + 1;

        long[] A = new long[size];
        List<P> m = mergeQueries(size, queries);

        long max = applyQuery(A, m);

        return max;
    }

    private static long applyQuery(long[] a, List<P> m) {
        long val = 0;
        long max = Long.MIN_VALUE;
        for(int i=0; i<a.length; i++){
            val = addVal(val, m.get(i));
            a[i] = val;
            max = Math.max(max, val);
            val = removeVal(val, m.get(i));
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

    static List<P> mergeQueries(int size, int[][] queries) {
        List<P> m = new ArrayList<>();

        for(int i =0; i<size; i++){
            m.add(new P(0,0));
        }

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

    static void addP(List<P> m, int pos, int value, boolean isStart) {
//        System.out.println(q + " pos " + pos + " val "+ value + " start "+ isStart);
            P p = m.get(pos);
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
