package com.apps.hackerrank;

public class CountingValeys {

    static int countingValleys(int n, String s) {
        char[] arr = s.toCharArray();
        int pos = 0;
        int valley = 0;
        for(int i = 0; i < arr.length; i++){
            int prevPos = pos;
            if(arr[i] == 'D'){
                pos--;
            } else {
                pos++;
            }
            if (prevPos < 0 && pos == 0){
                valley++;
            }
        }
        return valley;
    }

    public static void main(String[] args)  {

        System.out.println(countingValleys(8, "UDDDUDUUDDU"));

    }
}
