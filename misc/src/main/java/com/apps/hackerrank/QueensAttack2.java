package com.apps.hackerrank;

public class QueensAttack2 {

    public static void main(String[] args)  {
        System.out.println(collinear(40,40, 50,30,70,10));
    }

    static boolean collinear(int x1, int y1, int x2, int y2, int x3, int y3) {
        return (y1 - y2) * (x1 - x3) == (y1 - y3) * (x1 - x2);
    }
}
