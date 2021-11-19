package com.apps.leetcode;

import java.util.HashMap;
import java.util.Map;

public class CountSubArraysWithSumK {
//    https://leetcode.com/problems/subarray-sum-equals-k/
// static is not accepted by leetcode be carefull

    public static void main(String[] args) {
        int[] arr = { 1, 2, 3 };
        System.out.println(subarraySum(arr, 3));
    }

    public static int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> prefSum = new HashMap<>();
        int res = 0;
        int curSum = 0;

        for (int i = 0; i < nums.length; i++) {
            curSum = curSum + nums[i];
            if (curSum == k) {
                res++;
            }

            if(prefSum.containsKey(curSum - k) ){
                res += prefSum.get(curSum - k);
            }

            addToPrefSum(prefSum, curSum);
        }
        return res;
    }

    private static void addToPrefSum (Map<Integer, Integer> prefSum, int curSum) {
        if (prefSum.containsKey(curSum)) {
            prefSum.put(curSum, prefSum.get(curSum) + 1);
        } else {
            prefSum.put(curSum, 1);
        }
    }

}


