package com.apps.hackerrank.DaysOfCode;

public class Factorial {
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        System.out.println(factorial(5));
    }

    // Complete the factorial function below.
    static int factorial(int n) {
        if (n==0){
            return 0;
        }
        if (n==1){
            return 1;
        }
        return n * factorial(n - 1);
    }

}
