package com.apps.hackerrank;


import java.util.Arrays;
import java.util.List;

//https://www.hackerrank.com/challenges/bigger-is-greater/problem
public class BiggerIsGreater {


    public static String biggerIsGreater(String w) {
        w.compareTo(w);
        char[] arr = w.toCharArray();
        int arrBreak = findBreak(arr);

        return null;
    }

    private static int findBreak(char[] arr) {
        for(int i = arr.length - 1; i > 0 ; i--) {
            if(arr[i - 1] > arr[i]) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args)  {
        biggerIsGreater("abdeeeddklm");
    }

}
