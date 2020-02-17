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
//            if(el > ma0) {
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
        int [] A = {-3, 1, -8, 4, -1, 2, 1, -7, 5};
        f(A);
    }

    public static void f(int[] a) {

        int res[] = new int[a.length];
        int resRev[] = new int[a.length];
        int temp= 0;

        for (int i = 0; i < a.length -1; i++) {
            temp = a[i] + temp;
            res[i] = temp;
        }

        temp= 0;
        for (int i = a.length - 1; i > 0; i--) {
            temp = a[i] + temp;
            resRev[i] = temp;

        }

        int max = 0;

        for(int el : res){
            max = Integer.max(max, el);
        }

        for(int el : resRev){
            max = Integer.max(max, el);
        }

        System.out.println(max);
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

