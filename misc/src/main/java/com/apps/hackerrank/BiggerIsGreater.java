package com.apps.hackerrank;


import java.util.Arrays;
import java.util.List;

//https://www.hackerrank.com/challenges/bigger-is-greater/problem
public class BiggerIsGreater {


    public static String biggerIsGreater(String w) {
        char[] arr = w.toCharArray();
        int arrBreak = findBreak(arr);
        replace(arr, arrBreak);
        char[] beforeBreak = Arrays.copyOfRange(arr, 0, arrBreak + 1);
        char[] afterBreak = Arrays.copyOfRange(arr, arrBreak + 1, arr.length);
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

    private static void replace(char[] arr, int arrBreak) {
        char c = arr[arrBreak];
        char replacement = Character.MAX_VALUE;
        int pos = -1;
        for(int i = arr.length - 1; i > arrBreak ; i--) {
            if(arr[i] > c && arr[i] < replacement) {
                replacement = arr[i];
                pos = i;
            }
        }
        arr[pos] = arr[arrBreak];
        arr[arrBreak] = replacement;
    }

    public static void main(String[] args)  {
        biggerIsGreater("dkhc");
        biggerIsGreater("abdeeeddklm");
    }

}
//cdhk
//hcdk - correct