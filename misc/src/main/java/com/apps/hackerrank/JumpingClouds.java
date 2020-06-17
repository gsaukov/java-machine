package com.apps.hackerrank;

public class JumpingClouds {

    static int jumpingOnClouds(int[] A) {
        int jumps = 0;
        int pos = 0;
        while(true){
            if(pos<A.length-2 && A[pos+2] == 0){
                pos = pos + 2;
                jumps++;
            } else {
                pos++;
                jumps++;
            }
            if(pos>=A.length-1){
                return jumps;
            }
        }
    }

    public static void main(String[] args)  {
        int[] A = new int[]{0,0,1,0,0,1,0};
        System.out.println(jumpingOnClouds(A));
    }
}
