package com.apps.codility;

import java.util.ArrayList;
import java.util.List;

public class Peaks {

    public static void main(String[] args) {
        int[] A = new int[]{1,2,3,4,3,4,1,2,3,4,6,2};
        System.out.println(solution(A));
    }

    public static int solution(int[] A){
        if(A.length < 3){
            return 0;
        }

        List<P> peaks = new ArrayList<>();
        for (int i = 1; i<A.length-1; i++){
            if(isPeak(i, A)){
                peaks.add(new P(i, A[i]));
            }
        }

        for(int block = 2; block < A.length/2; block++){
            if(A.length%block ==0){
                int pos = 1;
                boolean found = true;
                for(P peak : peaks){
                    if(peak.pos/pos == block){
                        found = true;
                    } else {
                        found = false;
                    }
                    pos++;
                }
                if(found){
                    return A.length/block;
                }
            }
        }

        return 0;
    }

    public static boolean isPeak(int i, int[] A){
        return A[i] > A[i-1] && A[i] > A[i+1];
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
