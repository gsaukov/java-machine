package com.apps.codility;

import jnr.ffi.annotations.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EquiLeader {

    public static void main(String[] args) {
        int[] A = new int[]{0,1,0,0,0,0};
        System.out.println(findEquiLeader(A));

    }

    private static int findEquiLeader(int[] A) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for(int e: A){
            if(map.containsKey(e)){
                map.put(e, map.get(e) +1);
            } else {
                map.put(e, 1);
            }
        }

        List<Integer> leader = new ArrayList<>();
        for (Integer e: map.keySet()){
            Integer leftCount = map.get(e);
            Integer currentCount = 0;
            Integer length = A.length;
            if(leftCount<length/2){
                continue;
            }
            for (int i = 0; i<length;i++ ) {
                if(Integer.valueOf(A[i]).equals(e)){
                    currentCount++;
                    leftCount--;
                    if(currentCount>(i/2)&&(leftCount>((length-i)/2))){
                        leader.add(e);
                    }
                }
            }
        }

        return leader.size();
    }
}
