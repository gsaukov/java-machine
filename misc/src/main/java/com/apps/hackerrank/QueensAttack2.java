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
        if (!isCollinear(p.x, p.y, o.x, o.y, Q.x, Q.y)) //collinear
            return false;
        return (p.x<o.x && o.x<Q.x && p.y<o.y && o.y<Q.y) || //SW
                (p.x>o.x && o.x>Q.x && p.y>o.y && o.y>Q.y) || //NE
                (p.x==o.x && o.x==Q.x && p.y>o.y && o.y>Q.y) || //NN
                (p.x==o.x && o.x==Q.x && p.y<o.y && o.y<Q.y) || //SS
                (p.x>o.x && o.x>Q.x && p.y==o.y && o.y==Q.y) || //EE
                (p.x<o.x && o.x<Q.x && p.y==o.y && o.y==Q.y) || //WW
                (p.x<o.x && o.x<Q.x && p.y>o.y && o.y>Q.y) || //WN
                (p.x>o.x && o.x>Q.x && p.y<o.y && o.y<Q.y); // SE
    }

    static boolean isCollinear(int x1, int y1, int x2, int y2, int x3, int y3) {
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
        //obs [[y, x]...]
        int[][] obs = {
                {11, 12},
                {11, 14},
                {10, 13},
                {12, 13},
                {10, 14},
                {10, 12},
                {12, 14},
                {12, 12}};
//        int[][] obs2 = {{5, 5}, {4, 2}, {2, 3}};
        System.out.println(queensAttack(20, obs.length, 11, 13, obs));

//        System.out.println(queensAttack(5, obs.length, 2, 4, null));

    }
}
