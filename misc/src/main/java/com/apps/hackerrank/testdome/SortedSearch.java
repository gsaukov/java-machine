package com.apps.hackerrank.testdome;

import java.util.Arrays;

public class SortedSearch {
    public static int countNumbers(int[] sortedArray, int lessThan) {
        if (sortedArray.length == 0) return 0;
        if (sortedArray[0] >= lessThan) return 0;

        int num = Arrays.binarySearch(sortedArray, lessThan);
        if (num >= 0) {
            return num;
        }
        return (-num) - 1;
    }

    public static void main(String[] args) {
        System.out.println(SortedSearch.countNumbers(new int[] { 1, 3, 5, 7 }, 4));
    }

}
