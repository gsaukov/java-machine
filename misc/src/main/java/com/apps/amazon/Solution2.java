package com.apps.amazon;

import com.github.andrewoma.dexx.collection.ArrayList;
import com.github.andrewoma.dexx.collection.List;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Solution2 {


    public static void main(String[] args) {
        int[] a = {1, 3, 4, 2, 2, 2, 1, 1, 2};

        System.out.println(solution(a));
    }


    public static boolean solution(int[] A) {
        int totalSum = 0;
        for (int el : A) {
            totalSum += el;
        }

        int nextSum = totalSum;
        int chunk = totalSum / 3;
        while(chunk >= 3){
            int chunkSum = 0;
            int counter = 1;
            for (int el : A) {
                if(chunkSum == chunk){
                    System.out.println(chunkSum + " " + chunk);
                    chunkSum = 0;
                    counter++;
                } else if (chunkSum > chunk){
                    break;
                } else {
                    chunkSum = chunkSum + el;
                }
            }
            if(chunkSum == chunk && counter == 3){
                return true;
            }
            nextSum = nextSum - 3;
            chunk = nextSum / 3;
        }
        return false;
    }

    public static boolean solution2(int... A) {
        int totalSum = 0;
        for (int el : A) {
            totalSum += el;
        }


        int left = 0;
        int right = A.length - 1;
        int leftSum = A[left];
        int rightSum = A[right];
        while (left < right) {
            if (leftSum < rightSum) {
                left++;
                leftSum += A[left];
            } else if (leftSum > rightSum) {
                right--;
                rightSum += A[right];
            } else {
                int middleSum = totalSum - leftSum - rightSum;
                if ((middleSum - A[left + 1] - A[right - 1]) == leftSum) {
                    return true;
                } else if ((middleSum - A[left + 1] - A[left + 2]) == leftSum) {
                    return true;
                } else if ((middleSum - A[right - 1] - A[right - 2]) == leftSum) {
                    return true;
                } else {
                    leftSum += A[left];
                    left++;
                    rightSum += A[right];
                    right--;
                }
            }
        }
        return false;
    }

}

