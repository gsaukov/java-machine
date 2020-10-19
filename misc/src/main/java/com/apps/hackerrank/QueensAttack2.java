package com.apps.hackerrank;

//- n: an integer, the number of rows and columns in the board
//- k: an integer, the number of obstacles on the board
//- r_q: integer, the row number of the queen's position
//- c_q: integer, the column number of the queen's position

public class QueensAttack2 {

    private static P Q;
    private static P NN;
    private static P NE;
    private static P EE;
    private static P ES;
    private static P SS;
    private static P SW;
    private static P WW;
    private static P WN;

    static int queensAttack(int n, int k, int qY, int qX, int[][] obstacles) {
        Q = new P(qX, qY);
        initBorders(n);
        applyObstacles(obstacles);
        return 0;
    }

    static void initBorders(int n){
        NN = new P(Q.x, n);
        int min_NE = Math.min((n-Q.x), (n-Q.y));
        NE = new P(Q.x + min_NE, Q.y + min_NE);
        EE = new P(n, Q.y);
        int min_ES = Math.min((n-Q.x), (Q.y - 1));
        ES = new P(Q.x + min_ES, Q.y - min_ES);
        SS = new P(Q.x, 1);
        int min_SW = Math.min((Q.x - 1), (Q.y - 1));
        SW = new P(Q.x - min_ES, Q.y - min_ES);
        WW = new P(1, Q.y);
        int min_WN = Math.min((Q.x - 1), (n-Q.y));
        WN = new P(Q.x - min_ES, Q.y + min_ES);
    }


    static void applyObstacles(int[][] obstacles) {
        for(int i = 0; i < obstacles.length; i++) {
            P o = new P(obstacles[i][1], obstacles[i][0]);
            if (isBetween(NN, o)) {
                NN = o;
            } else if (isBetween(NE, o)) {
                NE = o;
            } else if (isBetween(EE, o)) {
                EE = o;
            } else if (isBetween(ES, o)) {
                ES = o;
            } else if (isBetween(SS, o)) {
                SS = o;
            } else if (isBetween(SW, o)) {
                SW = o;
            } else if (isBetween(WW, o)) {
                WW = o;
            } else if (isBetween(WN, o)) {
                WN = o;
            }
        }
    }

    private static boolean isBetween(P p, P o) {
        int dxc = o.x - p.x;
        int dyc = o.y - p.y;

        int dxl = Q.x - p.x;
        int dyl = Q.y - p.y;

        if ((dxc * dyl - dyc * dxl) != 0) //collinear
            return false;

        if (Math.abs(dxl) >= Math.abs(dyl))
            return dxl > 0 ?
                    p.x <= o.x && o.x <= Q.x :
                    Q.x <= o.x && o.x <= p.x;
        else
            return dyl > 0 ?
                    p.y <= o.y && o.y <= Q.y :
                    Q.y <= o.y && o.y <= p.y;
    }

    static boolean collinear(int x1, int y1, int x2, int y2, int x3, int y3) {
        return (y1 - y2) * (x1 - x3) == (y1 - y3) * (x1 - x2);
    }



    public static void main(String[] args)  {
        int[][] obs = {{3, 5}, {6, 4}, {6, 4}, {4, 5}, {7, 7}, {6, 6}};
        System.out.println(queensAttack(8,obs.length, 4,4, obs));
//        System.out.println(collinear(50,30,70,10,40,40));
//        Q = new P(40, 40);
//        P p = new P(50,30);
//        P o = new P(70,10);
//        System.out.println(isBetween(p, o));
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
