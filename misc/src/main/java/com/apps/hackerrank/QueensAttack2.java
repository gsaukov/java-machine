package com.apps.hackerrank;

//- n: an integer, the number of rows and columns in the board
//- k: an integer, the number of obstacles on the board
//- r_q: integer, the row number of the queen's position
//- c_q: integer, the column number of the queen's position

public class QueensAttack2 {

    private static P NN;
    private static P NE;
    private static P EE;
    private static P ES;
    private static P SS;
    private static P SW;
    private static P WW;
    private static P WN;

    static int queensAttack(int n, int k, int qY, int qX, int[][] obstacles) {
        NN = new P(qX, n);
        int min_NE = Math.min((n-qX), (n-qY));
        NE = new P(qX + min_NE, qY + min_NE);
        EE = new P(n, qY);
        int min_ES = Math.min((n-qX), (qY - 1));
        ES = new P(qX + min_ES, qY - min_ES);
        SS = new P(qX, 1);
        int min_SW = Math.min((qX - 1), (qY - 1));
        SW = new P(qX - min_ES, qY - min_ES);
        WW = new P(1, qY);
        int min_WN = Math.min((qX - 1), (n-qY));
        WN = new P(qX - min_ES, qY + min_ES);
        return 0;
    }

    public static void main(String[] args)  {
        System.out.println(queensAttack(8,0, 4,4,null));
        System.out.println(collinear(40,40, 50,30,70,10));
    }

    static boolean collinear(int x1, int y1, int x2, int y2, int x3, int y3) {
        return (y1 - y2) * (x1 - x3) == (y1 - y3) * (x1 - x2);
    }

    public static class P {
        int x;
        int y;
        P(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
}
