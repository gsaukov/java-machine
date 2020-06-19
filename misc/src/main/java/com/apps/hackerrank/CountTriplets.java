package com.apps.hackerrank;

import java.util.*;

public class CountTriplets {

    static long countTriplets(List<Long> arr, long r) {
        TreeMap<Long, Integer> vals = new TreeMap<>();
        for(Long v : arr){
            if (v==1 || v%r == 0){
                if(!vals.containsKey(v)){
                    vals.put(v,0);
                } else {
                    vals.put(v, vals.get(v) + 1);
                }

            }
        }

        int triplets = 0;
        if(r==1){
            for(Integer v : vals.values()){
                if(v>=2){
                    triplets +=v;
                }
            }
            return triplets;
        }

        Long[] A = new Long[vals.size()];
        vals.keySet().toArray(A);


        for(int i = 0; i<A.length-2; i++){
            if(A[i]*r == A[i+1] && A[i]*r*r == A[i+2]) {
                int count = getCount(A[i], vals, r);
                triplets +=count;
            }
        }

        return triplets;
    }

    private static int getCount(long l, TreeMap<Long, Integer> vals, long r) {
        int count = 1;
        for(long i = 0; i<=2; i++){
            count += vals.get(l);
            l=l*r;
        }
        return count;
    }

    public static void main(String[] args) {

        Long[] A = new Long[]{Long.valueOf(1),Long.valueOf(3), Long.valueOf(4), Long.valueOf(16), Long.valueOf(64)};

        System.out.println(countTriplets(Arrays.asList(A), 4l));

        Long[] B = new Long[]{Long.valueOf(1),Long.valueOf(2), Long.valueOf(2), Long.valueOf(4)};

        System.out.println(countTriplets(Arrays.asList(B), 2l));

        Long[] C = new Long[]{Long.valueOf(1),Long.valueOf(1), Long.valueOf(1), Long.valueOf(1)};

        System.out.println(countTriplets(Arrays.asList(C), 1l));
    }
}
