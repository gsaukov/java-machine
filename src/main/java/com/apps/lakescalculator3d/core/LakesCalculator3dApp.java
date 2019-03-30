package com.apps.lakescalculator3d.core;

import java.util.ArrayList;
import java.util.List;

public class LakesCalculator3dApp {

    static int[][] arrX =      {{1, 1, 1, 2, 2, 1, 1, 1},
                                {1, 1, 1, 1, 1, 1, 0, 0},
                                {1, 0, 0, 1, 1, 0, 1, 1},
                                {4, 3, 3, 3, 3, 0, 1, 5},
                                {1, 4, 1, 2, 5, 0, 1, 5},
                                {1, 4, 4, 1, 5, 4, 3, 0},
                                {1, 4, 1, 1, 1, 1, 4, 1},
                                {1, 2, 4, 4, 4, 4, 3, 1},
    };

    static int[][] hole =      {{1, 1, 1, 2, 2, 1, 1, 1},
                                {1, 1, 1, 1, 1, 1, 0, 0},
                                {1, 0, 0, 1, 1, 0, 1, 1},
                                {4, 3, 3, 3, 3, 0, 1, 5},
                                {1, 4, 1, 2, 5, 0, 1, 5},
                                {1, 4, 4, 1, 5, 4, 3, 0}, // bug
                                {1, 4, 1, 1, 1, 1, 1, 1},
                                {1, 2, 4, 4, 4, 4, 3, 1},
    };

    //TODO rebalance water levels with BFS or rebuild on BFS.

    public static void main(String[] args) throws Exception {
        RotateMatrix rotator = new RotateMatrix();
        LakesCalculator calculator = new LakesCalculator();
        List<List<Surface>> surfacesX = converter(hole); // both lists keep the same surfaces however Y is rotated 90 degrees against X
        List<List<Surface>> surfacesY = rotator.rotateSurfaces(surfacesX);
        printSurface(surfacesX);
        printSurface(surfacesY);
        for(List<Surface> surfaceX: surfacesX){
            calculator.calculate(surfaceX, true);
        }
        for(List<Surface> surfaceY: surfacesY){
            calculator.calculate(surfaceY, false);
        }
        calculateDepthes(surfacesX);
        printDepthes(surfacesX);
    }

    public static void printSurface (List<List<Surface>> surfaces ) {
        String res = "";
        for(List<Surface> row : surfaces){
            for (Surface col : row){
                res += (col + ", ");
            }
            System.out.println(res);
            res = "";
        }
        System.out.println("===================================");
    }

    public static void calculateDepthes (List<List<Surface>> surfaces ) {
        for(List<Surface> row : surfaces){
            for (Surface col : row){
                col.depth = Math.min(col.depthX, col.depthY);
            }
        }
    }

    public static void balanceDepthesBFS (List<List<Surface>> surfaces ) {
//
    }


    public static void printDepthes (List<List<Surface>> surfaces ) {
        String res = "";
        for(List<Surface> row : surfaces){
            for (Surface col : row){
                res += (col.depth + ", ");
            }
            System.out.println(res);
            res = "";
        }
        System.out.println("===================================");
    }

    public static List<List<Surface>> converter(int[][] arr) {
        int x = arr[1].length;
        int y = arr.length;

        List<List<Surface>> surfaces = new ArrayList<>();

        for (int j = 0; j < y; j++){
            List<Surface> rows = new ArrayList<>();
            for(int i = 0; i < x; i++){
                rows.add(new Surface(arr[j][i], i, j));
            }
            surfaces.add(rows);
        }
        return surfaces;
    }
}
