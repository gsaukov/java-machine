package com.apps.codility.challenges;

public class BallSwitchBoardZeta2011 {
    private static int BOTTOM_BORDER;
    private static int RIGHT_BORDER;
    private static final int DIRECTION_BOTTOM = -1;
    private static final int DIRECTION_RIGHT = 1;

    public static void main(String[] args) {
        int[][] A = new int[][]{{-1, 0, -1}, {-1, 0, 0}};
        System.out.println(solution(A, 4));
    }

    public static int solution(int[][] A, int K) {
        int bottom = 0;
        BOTTOM_BORDER = A.length;
        RIGHT_BORDER = A[0].length;
        int x = 0;
        int y = 0;
        for (int i = 0; i <= K; i++) {
            bottom += move(A, x, y, DIRECTION_BOTTOM);
        }
        return bottom;
    }

    private static int move(int[][] A, int x, int y, int direction) {
        while (true) {
            if (y > BOTTOM_BORDER) {
                return 1;
            }
            if (x > RIGHT_BORDER) {
                return 0;
            }

            if (A[x][y] == DIRECTION_BOTTOM) {
                A[x][y] = DIRECTION_RIGHT;
                move(A, x, y + 1, DIRECTION_BOTTOM);
            } else if (A[x][y] == DIRECTION_RIGHT) {
                A[x][y] = DIRECTION_BOTTOM;
                move(A, x, y + 1, DIRECTION_BOTTOM);
            }
        }
    }

}
