package com.apps.amazon;

import java.util.Arrays;
import java.util.Random;

public class ProcessorSpeedTest {

    public static void main(String[] args) {
        for (int j = 0; j<100; j++){
            int[] arr = new int[10000];
            Random r = new Random();
            for(int i = 0; i<10000; i++){
                arr[i] = r.nextInt(100);
            }
            long start = System.currentTimeMillis();
            findMinAbsSum(arr);
            long end = System.currentTimeMillis();
            System.out.println(end - start);
        }
    }



    static int[] dp;

    public static int findMinAbsSum(int[] A) {
        int arrayLength = A.length;
        int M = 0;
        for (int i = 0; i < arrayLength; i++) {
            A[i] = Math.abs(A[i]);
            M = Math.max(A[i], M);
        }
        int S = sum(A);
        dp = new int[S + 1];
        int[] count = new int[M + 1];
        for (int i = 0; i < arrayLength; i++) {
            count[A[i]] += 1;
        }
        Arrays.fill(dp, -1);
        dp[0] = 0;
        for (int i = 1; i < M + 1; i++) {
            if (count[i] > 0) {
                for(int j = 0; j < S; j++) {
                    if (dp[j] >= 0) {
                        dp[j] = count[i];
                    } else if (j >= i && dp[j - i] > 0) {
                        dp[j] = dp[j - i] - 1;
                    }
                }
            }
        }
        int result = S;
        for (int i = 0; i < Math.floor(S / 2) + 1; i++) {
            if (dp[i] >= 0) {
                result = Math.min(result, S - 2 * i);
            }
        }
        return result;
    }
    public static int sum(int[] array) {
        int sum = 0;
        for(int i : array) {
            sum += i;
        }
        return sum;
    }
}
