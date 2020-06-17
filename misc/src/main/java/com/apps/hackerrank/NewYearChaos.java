package com.apps.hackerrank;

import java.util.ArrayList;
import java.util.List;

public class NewYearChaos {
    static void minimumBribes(int[] A) {
        List<Integer> res = new ArrayList<>();
        List<Integer> a = new ArrayList<>();

        for (int e : A){
            a.add(e);
        }

        boolean changes = true;
        while(changes){
            changes = false;
            for(int i=a.size()-1; i>=0; i--){
                int bribe = 0;
                int e = a.get(i);
                if(e > i + 1){
                    bribe = e - (i + 1);
                    if(bribe > 2){
//                        System.out.println(e + " " + bribe);
                        System.out.println("Too chaotic");
                        return;
                    } else {
//                        System.out.println(e + " " + bribe);
                        res.add(bribe);
                        a.remove(i);
                        a.add(e - 1, e);
                        changes = true;
                        break;
                    }
                }
            }
        }

        int sum = 0;
        for (Integer e : res){
            sum += e;
        }
        System.out.println(sum);
    }

    public static void main(String[] args)  {
//        int[] A = new int[]{2, 1, 5, 3, 4};
        int[] A = new int[]{1, 2, 5, 3, 7, 8, 6, 4};
        minimumBribes(A);
    }

}

