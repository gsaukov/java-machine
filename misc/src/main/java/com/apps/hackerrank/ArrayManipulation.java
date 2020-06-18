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

    static long arrayManipulation2(int n, int[][] queries) {
        int size = n;

        if(queries[0].length == 2){
            size = queries[0][0];
            queries = Arrays.copyOfRange(queries, 1, queries.length);
        }

        size = size + 1;

        long[] A = new long[size];
        List<List<P>> m = mergeQueries(size, queries);

        long max = applyQuery2(A, m);

        return max;
    }

    private static long applyQuery2(long[] a, List<List<P>> m) {
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

    private static long addVal(long val, List<P> ps) {
        if(ps!=null){
            for(P p : ps){
                if(p.start){
                    val += p.value;
                }
            }
        }
        return val;
    }

    private static long removeVal(long val, List<P> ps) {
        if(ps!=null) {
            for (P p : ps) {
                if (!p.start) {
                    val -= p.value;
                }
            }
        }
        return val;
    }

    static List<List<P>> mergeQueries(int size, int[][] queries) {
        List<List<P>> m = new ArrayList<>();

        for(int i =0; i<size; i++){
            m.add(null);
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
        boolean start;
        int value;

        public P(boolean start, int value) {
            this.start = start;
            this.value = value;
        }
    }

    static void addP(List<List<P>> m, int pos, int value, boolean isStart) {
//        System.out.println(q + " pos " + pos + " val "+ value + " start "+ isStart);
        if(m.get(pos) == null){
            List<P> p = new ArrayList<>();
            p.add(new P(isStart, value));
            m.remove(pos);
            m.add(pos, p);
        } else {
            List<P> p = m.get(pos);
            p.add(new P(isStart, value));
        }
    }

    public static void main(String[] args) {

        int[][] A = new int[][]{{1, 5, 3},{4, 8, 7}, {6, 9, 1},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7},{4, 8, 7}};
//        int[][] A = new int[][]{{5, 3}, {1, 2, 100}, {2, 5, 100}, {3, 4, 100}};
        for(int i = 0; i<1000000; i++){
            arrayManipulation2(100, A);
        }
        System.out.println(arrayManipulation2(10, A));
    }



}
