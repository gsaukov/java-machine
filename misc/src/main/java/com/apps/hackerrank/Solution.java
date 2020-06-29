package com.apps.hackerrank;

import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        int i = 4;
        double d = 4.0;
        String s = "HackerRank ";

        Scanner scan = new Scanner(System.in);

        int j = scan.nextInt();
        double b = scan.nextDouble();

        String c = "";

        while(scan.hasNext()){
            c = scan.nextLine();
        }


        /* Declare second integer, double, and String variables. */

        /* Read and save an integer, double, and String to your variables.*/
        // Note: If you have trouble reading the entire String, please go back and review the Tutorial closely.

        /* Print the sum of both integer variables on a new line. */

        /* Print the sum of the double variables on a new line. */

        /* Concatenate and print the String variables on a new line;
        	the 's' variable above should be printed first. */

        System.out.println(j + i);
        System.out.println(b * d);
        System.out.println(s + c);

        scan.close();
    }
}
