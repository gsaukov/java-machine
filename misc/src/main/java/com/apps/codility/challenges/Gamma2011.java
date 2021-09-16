package com.apps.codility.challenges;

/*
* In this problem we consider only strings consisting of lower-case English letters (a−z).

A string is a palindrome if it reads exactly the same from left to right as it does from right to left. For example, these strings are palindromes:

aza
abba
abacaba
These strings are not palindromes:

zaza
abcd
abacada
Given a string S of length N, a slice of S is a substring of S specified by a pair of integers (p, q), such that 0 ≤ p < q < N. A slice (p, q) of string S is palindromic if the string consisting of letters S[p], S[p+1], ..., S[q] is a palindrome. For example, in a string S = abbacada:

slice (0, 3) is palindromic because abba is a palindrome,
slice (6, 7) is not palindromic because da is not a palindrome,
slice (2, 5) is not palindromic because baca is not a palindrome,
slice (1, 2) is palindromic because bb is a palindrome.
Write a function

class Solution { public int solution(String S); }

that, given a string S of length N letters, returns the number of palindromic slices of S. The function should return −1 if this number is greater than 100,000,000.

For example, for string S = baababa the function should return 6, because exactly six of its slices are palindromic; namely: (0, 3), (1, 2), (2, 4), (2, 6), (3, 5), (4, 6).

Write an efficient algorithm for the following assumptions:

N is an integer within the range [0..20,000];
string S consists only of lowercase letters (a−z).
*
*/

/*
* @see com.apps.hackerrank.t24test.PalindromeCounter
*/

public class Gamma2011 {

    public static void main(String[] args) {

        System.out.println(solution("baababa")); //6 palindromes (0, 3), (1, 2), (2, 4), (2, 6), (3, 5), (4, 6).
    }

    public static int solution(String S) {
        char str[] = S.toCharArray();
        int n = str.length;

        // create empty 2-D matrix that counts all
        // palindrome substring. dp[i][j] stores counts of
        // palindromic substrings in st[i..j]
        int dp[][] = new int[n][n];

        // P[i][j] = true if substring str[i..j] is
        // palindrome, else false
        boolean P[][] = new boolean[n][n];

        // palindrome of single length
        for (int i = 0; i < n; i++)
            P[i][i] = true;

        // palindrome of length 2
        for (int i = 0; i < n - 1; i++) {
            if (str[i] == str[i + 1]) {
                P[i][i + 1] = true;
                dp[i][i + 1] = 1;
            }
        }

        // Palindromes of length more than 2. This loop is
        // similar to Matrix Chain Multiplication. We start
        // with a gap of length 2 and fill the DP table in a
        // way that gap between starting and ending indexes
        // increases one by one by outer loop.
        for (int gap = 2; gap < n; gap++) {
            // Pick starting point for current gap
            for (int i = 0; i < n - gap; i++) {
                // Set ending point
                int j = gap + i;

                // If current string is palindrome
                if (str[i] == str[j] && P[i + 1][j - 1])
                    P[i][j] = true;

                // Add current palindrome substring ( + 1)
                // and rest palindrome substring (dp[i][j-1]
                // + dp[i+1][j]) remove common palindrome
                // substrings (- dp[i+1][j-1])
                if (P[i][j] == true)
                    dp[i][j] = dp[i][j - 1] + dp[i + 1][j]
                            + 1 - dp[i + 1][j - 1];
                else
                    dp[i][j] = dp[i][j - 1] + dp[i + 1][j]
                            - dp[i + 1][j - 1];
            }
        }
        printMatrix(dp);
        printMatrix(P);

        // return total palindromic substrings
        return dp[0][n - 1];
    }

    private static void printMatrix(int A[][]) {
        for(int row[] : A) {
            System.out.println();
            for(int n : row) {
                System.out.print(n + " ");
            }
        }
    }

    private static void printMatrix(boolean A[][]) {
        for(boolean row[] : A) {
            System.out.println();
            for(boolean n : row) {
                System.out.print(n + " ");
            }
        }
    }
}
