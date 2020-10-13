package com.apps.hackerrank;

import java.util.Arrays;

public class Solution2MergeArrays {

    private static int[] merge( int[] a, int[] b ) {
        int[] c = new int [a.length + b.length];
        int i = 0; //a
        int j = 0; //b
        while(i != a.length && j != b.length) {
            if (a[i] < b[j]) {
                c[i + j] = a[i];
                i++;

            } else {
                c[i + j] = b[j];
                j++;
            }
        }

        for(int k = j; k < b.length; k++) {
            c[k+i] = b[k];
        }

        for(int k = i; k < a.length; k++) {
            c[k+j] = a[k];
        }

        return c;
    }

    public static void main( String[] args ){
        int[] a = new int[]{1, 10, 25, 31, 33, 50};
        int[] b = new int[]{2, 4, 32, 60};
        int[] c = merge(a, b);
        System.out.println(Arrays.toString(c));}
}


