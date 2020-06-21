package com.apps.hackerrank.t24test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FootballScore {

    public static List<Integer> counts(List<Integer> teamA, List<Integer> teamB) {
        Integer[] A = new Integer[teamA.size()] ;
        A = teamA.toArray(A);
        Arrays.sort(A);

        List<Integer> res =  new ArrayList<>();
        for (Integer e : teamB){
            res.add(binarySearchCount(A, A.length, e));
        }
        return res;
    }

    static int binarySearchCount(Integer arr[], int n, int key)
    {
        int left = 0, right = n;

        int mid = 0;
        while (left < right) {
            mid = (right + left) >> 1;

            // Check if key is present in array
            if (arr[mid] == key) {

                // If duplicates are present it returns
                // the position of last element
                while (mid + 1 < n && arr[mid + 1] == key)
                    mid++;
                break;
            }

            // If key is smaller, ignore right half
            else if (arr[mid] > key)
                right = mid;

                // If key is greater, ignore left half
            else
                left = mid + 1;
        }

        // If key is not found in array then it will be
        // before mid
        while (mid > -1 && arr[mid] > key)
            mid--;

        // Return mid + 1 because of 0-based indexing
        // of array
        return mid + 1;
    }

    public static void main(String[] args)  {

        //System.out.println(featuredProduct(A));A

    }
}
