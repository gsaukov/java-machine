package com.apps.hackerrank.t24test;

public class PalindormeCounter {

    public static int countPalindromes(String s) {
        int count = 0;
        if (s == null || s.length() == 0) {
            return count;
        }
        boolean[][] dp = new boolean[s.length()][s.length()];
        for (int i = 0; i < s.length(); i++) {
            dp[i][i] = true;
            count++;
        }
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i - 1) == s.charAt(i)) {
                dp[i - 1][i] = true;
                count++;
            }
        }
        for (int j = 2; j < s.length(); j++) {
            for (int i = 0; i < j; i++) {
                if (dp[i + 1][j - 1] && s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = true;
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args)  {

        System.out.println(countPalindromes("aaa"));

    }
}
