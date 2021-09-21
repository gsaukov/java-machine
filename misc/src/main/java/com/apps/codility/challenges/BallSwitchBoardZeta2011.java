package com.apps.codility.challenges;

public class BallSwitchBoardZeta2011 {
    private static int BOTTOM_BORDER;
    private static int RIGHT_BORDER;

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
            bottom += move(A, x, y);
        }
        return bottom;
    }

    private static int move(int[][] A, int x, int y) {
        while (true) {
            if (y > BOTTOM_BORDER) {
                return 1;
            }
            if (x > RIGHT_BORDER) {
                return 0;
            }

            if(A[x][y] == 1) {

            }
        }
    }

}
