package com.apps.hackerrank;

//- n: an integer, the number of rows and columns in the board
//- k: an integer, the number of obstacles on the board
//- r_q: integer, the row number of the queen's position
//- c_q: integer, the column number of the queen's position
// https://www.hackerrank.com/challenges/queens-attack-2/problem

import java.util.Objects;

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
        return calculateMoves();
    }

    static void initBorders(int n) {
        NN = new P(Q.x, n);
        int min_NE = Math.min((n - Q.x), (n - Q.y));
        NE = new P(Q.x + min_NE, Q.y + min_NE);
        EE = new P(n, Q.y);
        int min_ES = Math.min((n - Q.x), (Q.y - 1));
        ES = new P(Q.x + min_ES, Q.y - min_ES);
        SS = new P(Q.x, 1);
        int min_SW = Math.min((Q.x - 1), (Q.y - 1));
        SW = new P(Q.x - min_SW, Q.y - min_SW);
        WW = new P(1, Q.y);
        int min_WN = Math.min((Q.x - 1), (n - Q.y));
        WN = new P(Q.x - min_WN, Q.y + min_WN);
    }


    static void applyObstacles(int[][] obstacles) {
        if(obstacles == null)
            return;
        for (int i = 0; i < obstacles.length; i++) {
            P o = new P(obstacles[i][1], obstacles[i][0]);
            if (isBetween(NN, o)) {
                NN = new P(o.x, o.y - 1);
            } else if (isBetween(NE, o)) {
                NE = new P(o.x - 1, o.y - 1);
            } else if (isBetween(EE, o)) {
                EE = new P(o.x - 1, o.y);
            } else if (isBetween(ES, o)) {
                ES = new P(o.x - 1, o.y + 1);
            } else if (isBetween(SS, o)) {
                SS = new P(o.x, o.y + 1);
            } else if (isBetween(SW, o)) {
                SW = new P(o.x + 1, o.y + 1);
            } else if (isBetween(WW, o)) {
                WW = new P(o.x + 1, o.y);
            } else if (isBetween(WN, o)) {
                WN = new P(o.x + 1, o.y - 1);
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

    static int calculateMoves() {
        int res = 0;
        res = res + distance(NN, SS);
        res = res + distance(NE, SW);
        res = res + distance(EE, WW);
        res = res + distance(ES, WN);
        return res;
    }

    static int distance(P p1, P p2) {
        return Math.max(Math.abs(p1.x - p2.x), Math.abs(p1.y - p2.y));
    }

    public static class P {
        int x;
        int y;

        P(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }

    public static void main(String[] args) {
        int[][] obs = {
                {20001, 20002},
                {20001, 20004},
                {20000, 20003},
                {20002, 20003},
                {20000, 20004},
                {20000, 20002},
                {20002, 20004},
                {20002, 20002},
                {564, 323}};
//        int[][] obs2 = {{5, 5}, {4, 2}, {2, 3}};
        System.out.println(queensAttack(88587, obs.length, 20001, 20003, obs));

//        System.out.println(queensAttack(5, obs.length, 2, 4, null));

    }
}
