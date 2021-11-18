package com.apps.leetcode;

import java.util.HashMap;
import java.util.Map;

public class CountSubArraysWithSumK {
//    https://leetcode.com/problems/subarray-sum-equals-k/
    private static Map<Integer, Integer> prefSum = new HashMap<>();

    public static void main(String[] args) {
        int[] arr = {1,2,3};
        System.out.println(subarraySum(arr, 1));
    }

    public static int subarraySum(int[] nums, int k) {
        int res = 0;
        int curSum = 0;

        if(nums.length == 1 && nums[0] == k) { //edge case
            return 1;
        }

        for (int i = 0; i < nums.length; i++) {
            curSum = curSum + nums[i];
            addToPrefSum(curSum);
        }

        curSum = 0;
        for (int i = 0; i < nums.length; i++) {
            curSum = curSum + nums[i];
            if(prefSum.containsKey(curSum - nums[i]) ){
                res++;
            }
        }
        return res;
    }

    private static void addToPrefSum (int curSum) {
        if (prefSum.containsKey(curSum)) {
            prefSum.put(curSum, prefSum.get(curSum) + 1);
        } else {
            prefSum.put(curSum, 1);
        }
    }

}


