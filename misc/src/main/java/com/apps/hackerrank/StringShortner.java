package com.apps.hackerrank;

import java.util.*;

public class StringShortner {

    static String superReducedString(String s) {
        StringBuilder sb = new StringBuilder();
        boolean isChnaged = true;
        char[] A = s.toCharArray();
        while(isChnaged && A.length > 0) {
            isChnaged = false;
            char cur = A[0];
            int curCount = 1;
            sb = new StringBuilder();
            for(int i = 1; i < A.length; i++) {
                if(cur == A[i]){
                    curCount++;
                } else {
                    if(curCount%2 != 0){
                        sb.append(cur);
                    }
                    if(curCount>1){
                        isChnaged = true;
                    }
                    cur = A[i];
                    curCount = 1;
                }
            }
            //last element
            if(curCount%2 != 0){
                sb.append(cur);
                if(curCount>1){
                    isChnaged = true;
                }
            }
            A = sb.toString().toCharArray();
        }
        String res = sb.toString();
        return res.equals("") ? "Empty String" : res;
    }

    public static void main(String[] args)  {
        System.out.println(superReducedString("ggppppuurrjjooddwwyyllmmvvffddmmppxxaabbddddooppxxgghhhhvvnneeqqyyttbbffvvjjiiaammmmddddhhyywwqqiijj"));
    }
}
