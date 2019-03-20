package com.apps.floodfill;

import static com.apps.floodfill.FloodFill.VOID;

public class Game {

//    static int[][] picture =   {{1, 1, 1, 1, 1, 1, 1, 1},
//                                {1, 1, 1, 1, 1, 1, 0, 0},
//                                {1, 0, 0, 1, 1, 0, 1, 1},
//                                {1, 2, 2, 2, 2, 0, 1, 0},
//                                {1, 1, 1, 2, 2, 0, 1, 0},
//                                {1, 1, 1, 2, 2, 2, 2, 0},
//                                {1, 1, 1, 1, 1, 2, 1, 1},
//                                {1, 1, 1, 1, 1, 2, 2, 1},
//    };


    static int[][] picture =   {{1, 1, 1, 2, 2, 1, 1, 1},
                                {1, 1, 1, 1, 1, 1, 0, 0},
                                {1, 0, 0, 1, 1, 0, 1, 1},
                                {4, 3, 3, 3, 3, 0, 1, 5},
                                {1, 1, 1, 3, 2, 0, 1, 5},
                                {1, 1, 1, 3, 2, 3, 3, 0},
                                {1, 2, 1, 1, 1, 3, 1, 1},
                                {1, 2, 1, 4, 4, 3, 3, 1},
    };



//    static int[][] picture = {{1, 1, 1},
//                              {1, 1, 1},
//                              {1, 0, 0},
//    };

    public static void main(String[] args) throws Exception {
        FloodFill floodFill = new FloodFill();
        int colorToPaint = 2;
        int colorToReplace = 1;
        do{
            floodFill.fill(picture, 0, 0, colorToReplace, colorToPaint);
            colorToReplace = colorToPaint;
            colorToPaint = floodFill.nextColorToPaint();
            printPicture();
        }while(colorToPaint != VOID);
    }

    public static void printPicture() {
        String res = "";
        for(int[] row : picture){
            for (int col : row){
                res += (col + ", ");
            }
            System.out.println(res);
            res = "";
        }
        System.out.println("===================================");
    }
}
