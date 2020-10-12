package com.apps.hackerrank;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class SherlockAndSquares {

    static int squares(int a, int b) {
        int start = (int)Math.ceil(Math.sqrt(a));
        int end = (int)Math.floor(Math.sqrt(b));

        if(end > 0 && end == start) {
            return 1;
        } else {
            return end - start + 1;
        }
    }

    public static void main(String[] args)  {
        System.out.println(squares(3, 9));
    }
}
