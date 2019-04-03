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
                                {1, 4, 4, 1, 5, 4, 3, 0},
                                {1, 4, 1, 1, 1, 1, 1, 1},
                                {1, 2, 4, 4, 4, 4, 3, 1},
    };


    static int[][] island =      {{5, 5, 5, 2, 2, 1, 1, 1},
                                  {5, 2, 2, 5, 5, 5, 0, 0},
                                  {5, 4, 1, 1, 1, 1, 5, 1},
                                  {4, 5, 3, 5, 5, 0, 5, 5},
                                  {1, 5, 1, 5, 5, 0, 2, 2},
                                  {1, 5, 4, 1, 2, 4, 5, 0},
                                  {1, 4, 5, 5, 5, 5, 1, 1},
                                  {1, 2, 4, 4, 4, 4, 3, 1},
    };

    public static void main(String[] args) throws Exception {
        RotateMatrix rotator = new RotateMatrix();
        LakesCalculator calculator = new LakesCalculator();
        List<List<Surface>> surfacesX = converter(island); // both lists keep the same surfaces however Y is rotated 90 degrees against X
        List<List<Surface>> surfacesY = rotator.rotateSurfaces(surfacesX);
        printSurface(surfacesX);
        printSurface(surfacesY);
        for(List<Surface> surfaceX: surfacesX){
            calculator.calculate(surfaceX, true);
        }
        for(List<Surface> surfaceY: surfacesY){
            calculator.calculate(surfaceY, false);
        }
        List<Surface> balanceCandidates = calculateDepthes(surfacesX);
        printDepthes(surfacesX);
        balanceDepthes(balanceCandidates, surfacesX); // Balancing we need for non bulging lakes of 'V', 'S', 'O' or any other crazy form.
        printDepthes(surfacesX);
    }

    public static void printSurface (List<List<Surface>> surfaces) {
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

    public static List<Surface> calculateDepthes (List<List<Surface>> surfaces ) {
        List<Surface> balanceCandidates = new ArrayList<>();
        for(List<Surface> row : surfaces){
            for (Surface col : row){
                col.depth = Math.min(col.depthX, col.depthY);
                if (col.depth > 0){
                    balanceCandidates.add(col);
                }
            }
        }
        return balanceCandidates;
    }

    public static void balanceDepthes (List<Surface> balanceCandidates, List<List<Surface>> surfaces) {
        boolean balanced = true;
        while(balanced){
            int balances = 0;
            for(Surface surface : balanceCandidates){ // its already n2 so no worries.
                int x = surface.x;
                int y = surface.y;
                balances += balance(surfaces, surface, x + 1, y);
                balances += balance(surfaces, surface, x - 1, y);
                balances += balance(surfaces, surface, x, y + 1);
                balances += balance(surfaces, surface, x, y - 1);
            }
            balanced = balances > 0;
        }

    }

    private static int balance(List<List<Surface>> surfaces, Surface surface, int x, int y) {
        Surface neighbor = surfaces.get(y).get(x);
        if (surface.depth == 0){  // we have some water.
            return 0;
        }
        if ((surface.depth + surface.val) > (neighbor.val + neighbor.depth)){ // and we are bigger than neighbors.
            int balanceDepth = (neighbor.val + neighbor.depth) - surface.val;
            surface.depth = Math.max(balanceDepth, 0); // set depth or 0 if it went negative.
            return 1;
        }
        return 0;
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
