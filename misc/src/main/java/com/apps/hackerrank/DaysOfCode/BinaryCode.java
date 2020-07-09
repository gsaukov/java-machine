package com.apps.hackerrank.DaysOfCode;

public class BinaryCode {
    public static void main(String[] args) {
        System.out.println(binary(439));
    }

    static int binary(int n) {
        char[] A = Integer.toBinaryString(n).toCharArray();
        int res = 0;
        int tempRes = 0;
        for (char e : A){
            if(e=='1'){
                tempRes++;
            } else {
                res = Math.max(res, tempRes);
                tempRes = 0;
            }
        }
        return Math.max(res, tempRes);
    }

}
