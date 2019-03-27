package com.apps.lakescalculator.controller;

import org.springframework.stereotype.Service;

@Service
public class InputParser {

    public int[] parse(String input){
        String[] strArr = input.split(",", -1);
        int[] intArr = new int[strArr.length];
        for(int i = 0; i < intArr.length; i++){
            intArr[i] = Integer.valueOf(fixInput(strArr[i]));
        }
        return intArr;
    }

    public String fixInput(String s){
        if(s.length() > 3){
            return s.substring(0, Math.min(s.length(), 3));
        } else if (s.length() == 0) {
            return "0";
        } else {
            return s;
        }
    }
}
