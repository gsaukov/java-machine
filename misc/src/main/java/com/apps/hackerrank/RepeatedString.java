package com.apps.hackerrank;

public class RepeatedString {
    static long repeatedString(String s, long n) {
        char[] arr = s.toCharArray();
        int size = arr.length;
        int a = 0;
        long res = 0;
        for(char c: arr){
            if(c == 'a'){
                a++;
            }
        }
        if(n%size==0){
            res = (n/size)*a;
        } else {
            long reminder = n%size;
            int reminderA = 0;
            for(int i = 0; i<reminder; i++){
                if(arr[i] == 'a'){
                    reminderA++;
                }
            }
            res = (n/size)*a + reminderA;
        }
        return res;
    }

    public static void main(String[] args)  {

        System.out.println(repeatedString("bab", 725261545450l));

    }
}
