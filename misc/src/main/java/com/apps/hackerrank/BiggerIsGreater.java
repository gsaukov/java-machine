package com.apps.hackerrank;


import java.util.Arrays;
import java.util.List;

//https://www.hackerrank.com/challenges/bigger-is-greater/problem
public class BiggerIsGreater {


    public static String biggerIsGreater(String w) {
        char[] arr = w.toCharArray();
        int arrBreak = findBreak(arr);
        char[] beforeBreak = Arrays.copyOfRange(arr, 0, arrBreak);
        char[] afterBreak = Arrays.copyOfRange(arr, arrBreak, arr.length);
        Arrays.sort(afterBreak);
        return new String(beforeBreak) + new String(afterBreak);
    }

    private static int findBreak(char[] arr) {
        for(int i = arr.length - 1; i > 0 ; i--) {
            if(arr[i - 1] < arr[i]) {
                return i - 1;
            }
        }
        return -1;
    }

    public static void main(String[] args)  {
        biggerIsGreater("dkhc");
        biggerIsGreater("abdeeeddklm");
    }

}
