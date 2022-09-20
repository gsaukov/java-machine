package com.apps.leetcode;

import java.util.Arrays;

//https://leetcode.com/problems/diagonal-traverse/

public class DiagonalTraverse {

    public static void main(String[] args) {
        int[][] A = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        DiagonalTraverse s = new DiagonalTraverse();
        System.out.println(Arrays.toString(s.findDiagonalOrder(A)));
    }

    int maxX;
    int maxY;
    int posX;
    int posY;
    boolean directionUp = true;

    public int[] findDiagonalOrder(int[][] mat) {
        maxX = mat.length;
        maxY = mat[0].length;
        posX = 0;
        posY = 0;
        directionUp = true;
        int[] res = new int[maxX * maxY];
        int resI = 0;
        while (posX < maxX && posY < maxY) {
            res[resI] = mat[posY][posX];
            resI++;
            if (canMove()) {
                move();
            } else {
                changeDirection();
            }
        }
        return res;
    }

    private boolean canMove() {
        if(directionUp) { // moving up and left
            if(posX + 1 < maxX && posY - 1 >= 0) {
                return true;
            } else {
                return false;
            }
        } else { // moving down and right
            if(posX - 1 >= 0 && posY + 1 < maxY) {
                return true;
            } else {
                return false;
            }
        }
    }

    private void move() {
        if(directionUp) { // moving up and left
            posX = posX + 1;
            posY = posY - 1;
        } else { // moving down and right
            posX = posX - 1;
            posY = posY + 1;
        }
    }

    private void changeDirection() {
        if(directionUp) {// moving up and left
            if (posX + 1 < maxX) {
                posX = posX + 1;
            } else {
                posY = posY + 1;
            }
        } else {
            if (posY + 1 < maxY) {
                posY = posY + 1;
            } else {
                posX = posX + 1;
            }
        }
        directionUp = directionUp ? false : true;
    }

}
