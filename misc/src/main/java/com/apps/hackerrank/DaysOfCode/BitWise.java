package com.apps.hackerrank.DaysOfCode;

public class BitWise {

    public static void main(String args[]) {
        findBitWise(8, 5);
    }

    static void findBitWise(int n, int k) {
        int max = 0;
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                int res = i&j;
                if(res < k && res > max) {
                    max = res;
                }
            }
        }

        System.out.println(max);
    }

}
