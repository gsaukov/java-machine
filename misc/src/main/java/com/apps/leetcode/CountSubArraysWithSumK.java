package com.apps.leetcode;

import java.util.HashMap;
import java.util.Map;

public class CountSubArraysWithSumK {
//    https://leetcode.com/problems/subarray-sum-equals-k/
    private static Map<Integer, Integer> prefSum = new HashMap<>();

    public static void main(String[] args) {
        int[] arr = {-1,-1,1};
        System.out.println(subarraySum(arr, 0));
    }

    public static int subarraySum(int[] nums, int k) {
        int res = 0;
        int curSum = 0;

        if(nums.length == 1 && nums[0] == k) { //edge case
            return 1;
        }

        curSum = 0;
        for (int i = 0; i < nums.length; i++) {
            curSum = curSum + nums[i];
            if (curSum == k) {
                res++;
            }

            if(prefSum.containsKey(curSum - k) ){
                res += prefSum.get(curSum - k);
            }

            addToPrefSum(curSum);
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


