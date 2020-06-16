package com.apps.codility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CountNonDivisible {

    public static void main(String[] args) {
        int[] A = new int[]{3,1,2,3,6};
        System.out.println(solution(A));
    }

    public static int[] solution(int[] A){
        HashMap<Integer, Integer> m = new HashMap<>();
        HashMap<Integer, Integer> c = new HashMap<>();
        for(Integer i : A){
            if(c.containsKey(i)){
                c.put(i, c.get(i) + 1) ;
            } else {
                m.put(i, 0);
                c.put(i, 1) ;
            }
        }

        for(Integer i : m.keySet()){
            for(Integer j : m.keySet()){
                if(i%j != 0){
                    m.put(i, m.get(i) + c.get(j));
                }
            }
        }
        int [] res = new int[A.length];
        for (int i = 0; i<A.length; i++){
            res[i] = m.get(A[i]);
        }
        return res;
    }

}
