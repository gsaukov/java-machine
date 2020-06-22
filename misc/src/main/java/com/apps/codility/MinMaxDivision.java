package com.apps.codility;

import java.util.ArrayList;
import java.util.List;

public class MinMaxDivision {
    public static void main(String[] args) {
        int[] A = new int[]{2,1,5,1,2,2,2};
        System.out.println(solution(3, 5, A));
    }

    public static int solution(int K, int M, int[] A){
        int total = 0;
        int maxEl = 0;
        for(int e: A){
            total += e;
            maxEl = Math.max(e, maxEl);
        }

        int start = Math.max(total/K, maxEl);

        for(int blockSum = start; blockSum<total; blockSum++){
            int sum = 0;
            List<Integer> res = new ArrayList<>();
            for(int i = 0; i<A.length; i++) {
                if((A[i] + sum) <= blockSum){
                    sum = A[i] + sum;
                } else {
                    res.add(sum);
                    sum = A[i];
                }
            }

            res.add(sum);
            if(res.size()<=K){
                int max = 0;
                for(Integer e : res){
                    max = Math.max(e, max);
                }
                return max;
            }
        }
        return 0;
    }
}
