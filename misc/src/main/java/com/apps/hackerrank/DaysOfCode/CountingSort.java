package com.apps.hackerrank.DaysOfCode;

import java.util.Arrays;
import java.util.List;

public class CountingSort {

    public static void main(String[] args) {
        int [] A = {12,5,7};
        countingSort(A);

        balancedSums(Arrays.asList(1, 2, 3, 3));
    }

    static int[] countingSort(int[] A) {
        int[] res =  new int[100];
        for(int i = 0; i < A.length; i++) {
            res[A[i]]++;
        }
        return res;
    }

    static String balancedSums(List<Integer> A) {
        int arrSum = 0;
        int currSum = 0;

        for(int i = 0; i < A.size(); i++) {
            arrSum += A.get(i);
        }

        for(int i = 0; i < A.size(); i++) {
            if((arrSum - A.get(i))/2 == currSum){
                return "YES";
            } else {
                currSum += A.get(i);
            }
        }

        return "NO";
    }

    static void countSort(List<List<String>> arr) {
        String [] res = new String[arr.size()/2];

        // cover with dash
        for (int i = 0;  i < res.length; i++) {
            Integer pos = Integer.valueOf(arr.get(i).get(0));
            res[pos] = populateRes(res[pos], "-");
        }

        // cover with values
        for (int i = res.length;  i < arr.size(); i++) {
            Integer pos = Integer.valueOf(arr.get(i).get(0));
            String val = arr.get(i).get(1);
            System.out.print(val + " ");
            res[pos] = populateRes(res[pos], val);
        }

        for(String s : res) {
            if(s!=null) {
                System.out.print(s + " ");
            }
        }
    }

    private static String populateRes(String res, String val){
        if(res == null) {
            return val;
        } else {
            return res + " " + val;
        }
    }
}
