package com.apps.hackerrank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwoDArrayDS {

    static int hourglassSum(int[][] A) {
        List<Integer> res = new ArrayList<>();

        for(int i = 0; i<=A.length - 3; i++){
            for(int j = 0; j<=A[i].length - 3; j++){
                res.add(sum(i, j, A));
            }
        }

        int max = Integer.MIN_VALUE;

        for (Integer e: res){
            max = Math.max(e, max);
        }

        return max;
    }

    public static int sum(int left, int top, int[][] arr) {
        int right = left + 2;
        int bot = top + 2;
        int sum = 0;
        for(int col = left; col<=right; col++){
            for(int row = top; row<=bot; row++){
                if((col == left && row == top + 1) || (col == right && row == top + 1)){

                } else {
                    sum += arr[row][col];
                }
            }
        }

        return sum;
    }

    public static void main(String[] args)  {
//        int[][] A = {{1, 1, 1, 0, 0, 0},
//                     {0, 1, 0, 0, 0, 0},
//                     {1, 1, 1, 0, 0, 0},
//                     {0, 0, 2, 4, 4, 0},
//                     {0, 0, 0, 2, 0, 0},
//                     {0, 0, 1, 2, 4, 0},
//                };
        int[][] A =
               {{-1, -1,  0, -9, -2, -2},
                {-2, -1, -6, -8, -2, -5},
                {-1, -1, -1, -2, -3, -4},
                {-1, -9, -2, -4, -4, -5},
                {-7, -3, -3, -2, -9, -9},
                {-1, -3, -1, -2, -4, -5},
        };

        System.out.println(hourglassSum(A));

    }


}
