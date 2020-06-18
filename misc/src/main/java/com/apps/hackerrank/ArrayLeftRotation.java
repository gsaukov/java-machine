package com.apps.hackerrank;

import java.util.Arrays;

public class ArrayLeftRotation {
    public static void main(String[] args) {
        int[] A = new int[]{0,1,0,0,0,0};
        System.out.println(Arrays.toString(rotLeft(A, 2)));
    }


    static int[] rotLeft(int[] A, int K){

        int m = getMove(A, K);

        int[] res =  new int[A.length];

        for(int i=0; i<A.length; i++){
            if(i - m >= 0){
                res[i-m] = A[i];
            } else {
                res[A.length+(i-m)] = A[i];
            }
        }

        return res;
    }

    private static int getMove(int[] A, int K){
        if(A.length >= K){
            return K;
        } else {
            return K%A.length;
        }
    }
}
