package com.apps.codility;

import java.util.ArrayList;
import java.util.List;

public class Flags {
    public static void main(String[] args) {
        int[] A = new int[]{1, 5, 3, 4, 3, 4, 1, 2, 3, 4, 6, 2};
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

        List<List<P>> success = new ArrayList<>();

        for (int k = 0; k < peaks.size() + 1; k++){
            List<P> placed =  new ArrayList<>();
            for (int i = 0; i < k; i++){
                tryPlace(placed, peaks, i, k);
            }

            success.add(placed);
        }

        int maxPlaced = 0;
        for(List<P> e : success){
            maxPlaced = Math.max(e.size(), maxPlaced);
        }

        return maxPlaced;
    }

    private static void tryPlace(List<P> placed, List<P> peaks, int i, int k) {
        if(placed.isEmpty()){
            placed.add(peaks.get(i));
        } else {
            P peak = peaks.get(i);
            P before = placed.get(placed.size()-1); // compare with last placed peak.
            if((peak.pos - before.pos) >= k){
                placed.add(peak);
            }
        }
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
