package com.apps.leetcode;

import java.util.Arrays;

public class SearchInRotatedSortedArray {

    public static void main(String[] args) {
        int[] A = {4, 5, 8, 9, 10, 11, 0, 1, 2, 3};
        SearchInRotatedSortedArray s = new SearchInRotatedSortedArray();
        s.search(A, 10);
    }

    public int search(int[] nums, int target) {
        int breach = breachPoint(nums);

        int left;
        int right;

        if(breach == -1){
            left = 0;
            right = nums.length-1;
        } else if (target >= nums[0] && target <= nums[breach]) {
            left = 0;
            right = breach;
        } else {
            left = breach + 1;
            right = nums.length-1;
        }

        int mid = (left + right) / 2;
        while (left < right) {

            if (nums[mid] == target){
                return mid;
            }

            if (nums[left] == target){
                return left;
            }

            if (nums[right] == target){
                return right;
            }

            if (nums[left] > nums[mid]) { //on the left
                right = mid;
                mid = (left + right) / 2;
            } else { // breach is on the right
                left = mid;
                mid = (left + right) / 2;
            }
        }

        return -1;
    }


    public int findInRange(int target, int[] nums, int left, int right) {

        return 0;
    }

    public int breachPoint(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        int mid = (left + right) / 2;
        if(nums[left] < nums[right]){
            return -1; // already sorted.
        }

        while (left < right) {
            if (nums[mid] > nums[mid + 1]){
                return mid;
            }
            if (nums[mid] < nums[mid - 1]){
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
