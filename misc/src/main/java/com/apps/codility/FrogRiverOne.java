package com.apps.codility;

import java.util.HashSet;

public class FrogRiverOne {
    public static void main(String[] args) {
        int[] A = new int[]{1,3,1,4,2,3,5,4};
        System.out.println(solution(5, A));
    }

    public static int solution(int X, int[] A) {

        HashSet<Integer> road = new HashSet<>();

        for (int i = 0; i < A.length; i++){
            road.add(A[i]);
            if(canJump(road, X)){
                return i;
            }
        }
        return -1;
    }

    private static boolean canJump(HashSet<Integer> road, int X) {
        if(road.size()<X){
            return false;
        } else {
            for(int j=1; j<=X; j++){
                if(!road.contains(j)){
                    return false;
                }
            }
        }
        return true;
    }
}
