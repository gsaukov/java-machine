package com.apps.hackerrank;

public class StringAppendAndDelete {

    static String appendAndDelete(String s, String t, int k) {


        if(k - Math.abs(s.length() - t.length()) < 0) {
            return "No";
        }
        int minLenght = Math.min(s.length(), t.length());
        int i = 0;
        boolean same = true;
        while (same && i < minLenght) {
            if(s.charAt(i) == t.charAt(i)){
                i++;
            } else {
                same = false;
            }
        }
        int left = ((s.length() - i) + (t.length() - i));
        if(left > k) {
            return "No";
        } else {
            if((s.length()+t.length()-2*i)%2==k%2) {
                return "Yes";
            }else if((s.length()+t.length()-k)<0){
                return "Yes";
            } else {
                return "No";
            }

        }

    }


    public static void main(String[] args)  {
        System.out.println(appendAndDelete("hackerhappy", "hackerrank", 9));
    }
}
