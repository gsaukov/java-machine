package com.apps.hackerrank.DaysOfCode;

public class FindPrime {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        int [] A = {12,5,7};
        findPrime(A);
    }

    private static void findPrime(int[] A) {
        for(int e : A){
            if(Math.abs(e)==1){
                System.out.println("Not prime");
            }else if (Math.abs(e)<=2){
                System.out.println("Prime");
            } else {
                for(int i = 2; i < e; i++) {
                    if(e%i==0){
                        System.out.println("Not prime");
                        break;
                    }
                    if(i>(e/i+1)){
                        System.out.println("Prime");
                        break;
                    }
                }
            }
        }
    }
}
