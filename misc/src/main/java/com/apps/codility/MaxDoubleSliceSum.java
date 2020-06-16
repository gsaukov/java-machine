package com.apps.codility;

import java.util.Arrays;

public class MaxDoubleSliceSum {

    public static void main(String[] args) {
//        int [] A = {9, 10, -9, 3, 9, 7, -11, 9, 11, 11, -3, 0, 5, 0};
//        solution(A);
        int [] B = {3, 2, -6, -1, 4, 5, -1, -5};
        System.out.println(solution(B));
    }

    public static int solution(int[] A){

        int cutPosLeft=0;
        int maxLeft=0;
        for (int i=1; i< A.length-1; i++){
            if((maxLeft+A[i])>0){
                maxLeft = maxLeft+A[i];
            } else {
                maxLeft = 0;
                cutPosLeft = i;
            }
        }

        int cutPosRight=A.length-1;
        int maxRight=0;
        for (int i=A.length-2; i>0; i--){
            if((maxRight+A[i])>0){ //not really working if array is total sum is negative
                maxRight = maxRight+A[i];
            } else {
                maxLeft = 0;
                cutPosRight = i;
            }
        }

        int[] res = Arrays.copyOfRange(A, cutPosLeft + 1, cutPosRight);

        int minVal = Integer.MAX_VALUE;
        int maxSum = 0;
        for(int e : res){
            minVal = Math.min(minVal, e);
            maxSum += e;
        }

        if(minVal<0){
            return maxSum + Math.abs(minVal);
        } else {
            return maxSum - minVal;
        }
    }
}
