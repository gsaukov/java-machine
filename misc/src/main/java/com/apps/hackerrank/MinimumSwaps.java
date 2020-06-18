package com.apps.hackerrank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinimumSwaps {

    static int minimumSwaps(int[] A) {

        // don't mutate the original array
        int[] list = Arrays.copyOf(A, A.length);

        // length of the list
        int listLen = list.length;
        // state - total swap

        int swap = 0;

        for (int i = 0; i < listLen; i++) {
            // for each iteration, we find the
            // number that should go to the correct position
            int idToSwap = -1;
            int correctNumber = i + 1;
            // iterate through starting next item
            // and find if the correct number is misplaced
            for (int j = i + 1; j < listLen; j++) {
                // if it is misplaced, swap it
                if (correctNumber == list[j]) {
                    idToSwap = j;
                    break;
                }
            }
            if (idToSwap>-1) {
                swap(list, i, idToSwap);
                swap++;
            }
        }
//    System.out.println(totalSwap);

//        int swap = 0 ;
//        boolean[] visited = new boolean[A.length];
//        for(int i=0; i< A.length; i++) {
//            int j=i, count = 0;
//            while(!visited[j]) {
//                visited[j]=true;
//                j=A[j]-1;
//                count++;
//            }
//            if(count!=0) {
//                swap += count-1;
//            }
//        }


//        System.out.println(swap);
        return swap;
    }

    public static final void swap (int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void main(String[] args)  {
//        int[] A = new int[]{2, 1, 5, 3, 4};
        int[] A = new int[]{7, 1, 3, 2, 4, 5, 6};
//        int[] A = new int[]{1,2,3,4,5,6};
        minimumSwaps(A);
    }
}
