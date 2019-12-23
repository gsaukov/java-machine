package com.apps.amazon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Solution {
//    public static int solution(int[] A) {
//        Arrays.sort(A);
//        int res = 1;
//
//        for(int el : A){
//            if(el > 0) {
//                if(res == el){
//                    res = el + 1;
//                }
//            }
//        }
//        return res;
//        // write your code in Java SE 8
//    }

    public int solution(int[] A) {
        Map<Integer, Integer> resMap = new HashMap<>();

        if(A.length == 0 || A.length % 2 == 0){
            throw new IllegalArgumentException("Array is empty or not odd.");
        }

        for(int el : A){
            Integer val = resMap.get(el);
            if (val != null){
                resMap.put(el, val + 1);
            } else {
                resMap.put(el, 1);
            }
        }

        for(Map.Entry<Integer, Integer> entry : resMap.entrySet()){
            if(entry.getValue() % 2 != 0) {
                return entry.getKey();
            }
        }

        throw new IllegalArgumentException("No odd value found");
    }



    public static void main(String[] args) {
//        System.out.println(solution(74901729));
        int [] A = {9, 3, 9, 3, 9, 7, 9, 11, 11, 0, 0, 0, 0};
        solution(74901729);
    }


    public static int solution(int N) {

        char[] bits = Integer.toString(N,2).toCharArray();

        int res = 0;
        int current = 0;

        for (char bit : bits) {
            if(bit == '1'){ // 1
                if (current > res){
                    res = current;
                }
                current = 0;
            } else { // 0
                current++;
            }
        }

        return res;
    }





}

