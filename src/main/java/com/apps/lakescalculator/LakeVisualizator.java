package com.apps.lakescalculator;

import java.util.List;

public class LakeVisualizator {

    public static void visualize (Lake lake){

        if(lake.isTotal()){
            return;
        }

        List<Surface> surfaces = lake.getLakeSurface();
        int x = surfaces.size();
        int y = lake.getMaxHeight();
        int g = lake.getSeaLevel() - lake.getMaxDepth();

        char[][] array = new char[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                array[i][j] = '.'; // fill air
            }
        }

        for (int j = 0; j < y; j++){
            for(int i = 0; i < surfaces.size(); i++){
                if(surfaces.get(i).val <= j){
                    array[i][j] = '#';
                }
            }
        }


//        for (int i = 0; i < x; i++) {
//            int val = surfaces.get(i).val;
//            for (int j = 0; j < val; j++){
//                array[i][j] = '#'; //fill ground
//            }
//        }
//
//        for (int i = 0; i < x; i++) {
//            int depth = surfaces.get(i).depth;
//            for (int j = g; j < depth; j++){
//                array[i][j] = '.'; //fill water
//            }
//        }

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                System.out.print(array[i][j]); //demonstrate
            }
            System.out.println();
        }

    }
}
