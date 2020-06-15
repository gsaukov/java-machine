package com.apps.codility;

import java.util.ArrayList;
import java.util.List;

public class TapeEquilibrium {


    public static void main(String[] args) {
        int[] A = new int[]{3,1,2,4,3};
        System.out.println(solution(A));
    }

    public static int solution(int[] A) {
        if(A==null||A.length==0){
            return 0;
        }

        int s = 0;

        for(int e: A){
            s += e;
        }

        List<P> res = new ArrayList<>();

        int currentSum = 0;
        for(int i = 0; i<A.length; i++) {
            currentSum += A[i];
            res.add(new P(i, Math.abs((s-currentSum)-currentSum)));
        }

        int min = Integer.MAX_VALUE;

        for(P p : res){
            min = Math.min(min, p.val);
        }

        return min;
    }

    public static class P {
        int pos;
        int val;
        P(int pos, int val){
            this.pos = pos;
            this.val = val;
        }
    }
}
