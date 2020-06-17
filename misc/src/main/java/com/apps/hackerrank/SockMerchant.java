package com.apps.hackerrank;

import java.util.HashMap;

public class SockMerchant {

    static int sockMerchant(int n, int[] A) {
        HashMap<Integer, Integer> m = new HashMap<>();

        for (int i = 0; i<A.length; i++) {
            int color = A[i];
            if(m.containsKey(color)){
                m.put(color, m.get(color) + 1);
            } else {
                m.put(color, 1);
            }
        }

        int pairs = 0;

        for(Integer e: m.values()){
            int num = e/2;
            pairs += num;
        }

        return pairs;
    }

    public static void main(String[] args)  {
        int[] A = new int[]{2,1,5,1,2,2,2};
        System.out.println(sockMerchant(3, A));

    }
}
