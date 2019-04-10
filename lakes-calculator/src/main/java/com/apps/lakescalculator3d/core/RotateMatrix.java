package com.apps.lakescalculator3d.core;

import java.util.ArrayList;
import java.util.List;

public class RotateMatrix {

    public List<List<Surface>> rotateSurfaces(List<List<Surface>> originalArr) {

        int totalRowsOfRotatedMatrix = originalArr.get(0).size(); //Total columns of Original Matrix
        int totalColsOfRotatedMatrix = originalArr.size(); //Total rows of Original Matrix

        List<List<Surface>> rotatedSurfaces = prepare2dArrayList(totalColsOfRotatedMatrix, totalRowsOfRotatedMatrix);
        for (int i = 0; i < totalColsOfRotatedMatrix; i++) {
            for (int j = 0; j < totalRowsOfRotatedMatrix; j++) {
                rotatedSurfaces.get(j).set((totalColsOfRotatedMatrix-1)- i, originalArr.get(i).get(j));
            }
        }
        return rotatedSurfaces;
    }

    private List<List<Surface>> prepare2dArrayList(int x, int y){
        List<List<Surface>> cols = new ArrayList<>();
        for(int i = 0; i < y; i++){
            List<Surface> rows = new ArrayList<Surface>();
            cols.add(rows);
            for (int j = 0; j < x; j++) {
                rows.add(null);
            }
        }
        return cols;
    }

    //Rotate Matrix to 90 degree toward Right(clockwise)
    public int[][] rotateMatrixBy90DegreeClockwise(int[][] matrix) {

        int totalRowsOfRotatedMatrix = matrix[0].length; //Total columns of Original Matrix
        int totalColsOfRotatedMatrix = matrix.length; //Total rows of Original Matrix

        int[][] rotatedMatrix = new int[totalRowsOfRotatedMatrix][totalColsOfRotatedMatrix];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                rotatedMatrix[j][ (totalColsOfRotatedMatrix-1)- i] = matrix[i][j];
            }
        }
        return rotatedMatrix;
    }

    //Rotate Matrix to 90 degree toward Left(counter clockwise)
    public int[][] rotateMatrixBy90DegreeCounterClockwise(int[][] matrix) {

        int totalRowsOfRotatedMatrix = matrix[0].length; //Total columns of Original Matrix
        int totalColsOfRotatedMatrix = matrix.length; //Total rows of Original Matrix

        int[][] rotatedMatrix = new int[totalRowsOfRotatedMatrix][totalColsOfRotatedMatrix];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                rotatedMatrix[(totalRowsOfRotatedMatrix-1)-j][i] = matrix[i][j];
            }
        }
        return rotatedMatrix;
    }

    private static void printMatrix(int[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "   ");
            }
            System.out.println();
        }
    }
}
