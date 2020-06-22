package com.apps.codility;

import java.util.Arrays;

public class CyclicRotation {

    public static void main(String[] args) {
        int[] A = new int[]{0,1,0,0,0,0};
        System.out.println(Arrays.toString(solution(A, 2)));
    }


    public static int[] solution(int[] A, int K){

        int m = getMove(A, K);

        int[] res =  new int[A.length];

        for(int i=0; i<A.length; i++){
            if(i + m < A.length){
                res[i+m] = A[i];
            } else {
                res[(i+m)-A.length] = A[i];
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
