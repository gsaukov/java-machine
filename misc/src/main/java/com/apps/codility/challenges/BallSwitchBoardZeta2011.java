package com.apps.codility.challenges;

//Could be solved with log, redesign with finding the path tree;

public class BallSwitchBoardZeta2011 {
    private static int BOTTOM_BORDER;
    private static int RIGHT_BORDER;
    private static final int DIRECTION_BOTTOM = -1;
    private static final int DIRECTION_RIGHT = 1;

    public static void main(String[] args) {
        int[][] A = new int[][]{
                {-1, 0, -1},
                {1, 0, 0} };
        System.out.println(solution(A, 4));
    }

    public static int solution(int[][] A, int K) {
        int bottom = 0;
        BOTTOM_BORDER = A.length - 1;
        RIGHT_BORDER = A[0].length - 1;
        int x = 0;
        int y = 0;
        for (int i = 0; i < K; i++) {
            bottom += move(A, y, x, DIRECTION_BOTTOM);
        }
        return bottom;
    }

    private static int move(int[][] A, int y, int x, int direction) {
        if (y > BOTTOM_BORDER && x == RIGHT_BORDER) {
            printMatrix(A);
            System.out.println(y + " " + x);
            return 1;
        }

        if (y > BOTTOM_BORDER && x < RIGHT_BORDER) {
            printMatrix(A);
            System.out.println(y + " " + x);
            return 0;
        }

        if (x > RIGHT_BORDER) {
            printMatrix(A);
            System.out.println(y + " " + x);
            return 0;
        }

        if (A[y][x] == DIRECTION_BOTTOM) {
            A[y][x] = DIRECTION_RIGHT;
            return move(A, y + 1, x, DIRECTION_BOTTOM);
        } else if (A[y][x] == DIRECTION_RIGHT) {
            A[y][x] = DIRECTION_BOTTOM;
            return move(A, y, x + 1, DIRECTION_RIGHT);
        } else {
            if (direction == DIRECTION_BOTTOM) {
                return move(A, y + 1, x, direction);
            } else {
                return move(A, y, x + 1, direction);
            }
        }
    }

    private static void printMatrix(int A[][]) {
        System.out.print("############################");
        for(int row[] : A) {
            System.out.println();
            for(int n : row) {
                System.out.print(n + " ");
            }
        }
        System.out.println();
    }

}
