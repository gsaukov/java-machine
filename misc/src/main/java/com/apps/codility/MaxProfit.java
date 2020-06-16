package com.apps.codility;

import java.util.*;

public class MaxProfit {

    public static void main(String[] args) {
        int [] A = {3, 2, 6, 1, 4, 5, 1, 5};
        System.out.println(solution(A));
    }

    public static int solution(int[] A){

        int maxDeal = 0;
        int lowest = A[0];

        for(int i = 1; i<A.length; i++) {
            if(lowest > A[i]){
                lowest = A[i];
            } else {
                maxDeal = Math.max(maxDeal, A[i]-lowest);
            }
        }


        return maxDeal;
    }

}
