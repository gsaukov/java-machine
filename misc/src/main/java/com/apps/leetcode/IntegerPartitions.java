package com.apps.leetcode;

import java.util.Arrays;

public class IntegerPartitions {

//    https://leetcode.com/problems/array-partition-i/

    public static void main(String[] args) {
        int[] arr = {6,2,6,5,1,2};
        System.out.println(arrayPairSum(arr));
    }

    public static int arrayPairSum(int[] nums) {
        Arrays.sort(nums);
        int maxMinSum = 0;
        for (int i = 1; i < nums.length;) {
            int curMin = Math.min(nums[i-1], nums[i]);
            maxMinSum = maxMinSum + curMin;
            i = i + 2;
        }
        return maxMinSum;
    }

}
