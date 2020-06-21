package com.apps.hackerrank;

import java.util.Arrays;

public class MergeSortCountingInteractions {


    static int countInversions(int[] A) {
        int inv_count = 0;
        for (int i = 0; i < A.length - 1; i++)
            for (int j = i + 1; j < A.length; j++)
                if (A[i] > A[j])
                    inv_count++;

        return inv_count;

    }


    public static void main(String[] args)  {
        int[] A = new int[]{0,0,1,0,0,1,0};
        System.out.println(countInversions(A));
    }
}
