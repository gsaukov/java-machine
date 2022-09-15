package com.apps.leetcode;

import java.util.Arrays;

//https://leetcode.com/problems/search-in-rotated-sorted-array/

public class SearchInRotatedSortedArray {

    public static void main(String[] args) {
        int[] A = {4, 5, 8, 9, 10, 11, 0, 1, 2, 3};
        int[] A1 = {9,1,2,3,4,5,6,7,8};
        SearchInRotatedSortedArray s = new SearchInRotatedSortedArray();
        s.search(A1, 9);
    }

    public int search(int[] nums, int target) {
        if(nums.length == 0){
            return -1;
        }

        if(nums.length < 4){
            for(int i = 0; i < nums.length; i++){
                if(nums[i] == target) {
                    return i;
                }
            }
            return -1;
        }

        int breach = breachPoint(nums);

        int left;
        int right;

        if (breach == -1) {
            left = 0;
            right = nums.length - 1;
        } else if (target >= nums[0] && target <= nums[breach]) {
            left = 0;
            right = breach;
        } else {
            left = breach + 1;
            right = nums.length - 1;
        }

        int mid = (left + right) / 2;
        while (left <= right) {

            if (nums[mid] == target) {
                return mid;
            }

            if (nums[left] == target) {
                return left;
            }

            if (nums[right] == target) {
                return right;
            }

            if (right - left < 3) {
                return -1;
            }

            if (nums[mid] > target) { //target on the left
                right = mid;
                mid = (left + right) / 2;
            } else { // target is on the right
                left = mid;
                mid = (left + right) / 2;
            }
        }

        return -1;
    }

    public int breachPoint(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        int mid = (left + right) / 2;
        if (nums[left] < nums[right]) {
            return -1; // already sorted. no breach.
        }

        while (left < right) {
            if (nums[mid] > nums[mid + 1]) {
                return mid;
            }
            if (nums[mid] < nums[mid - 1]) {
                return mid - 1;
            }

            if (nums[left] < nums[mid]) { // breach is on the right
                left = mid;
                mid = (left + right) / 2;
            } else { // breach is on the right
                right = mid;
                mid = (left + right) / 2;
            }
        }

        return mid;
    }

}
