package com.apps.lakescalculator;

import java.util.List;

public class LakeVisualizator {

    public String visualize (Lake lake){

        if(lake.isTotal()){
            return "";
        }

        List<Surface> surfaces = lake.getLakeSurface();
        int x = surfaces.size();
        int y = lake.getMaxHeight();

        char[][] array = new char[y][x];

        for (int j = 0; j < y; j++){
            for(int i = 0; i < x; i++){
                if(surfaces.get(i).val > j){
                    array[j][i] = '#'; // fill ground
                } else if (surfaces.get(i).val + surfaces.get(i).depth > j){
                    array[j][i] = '~'; // fill water
                } else {
                    array[j][i] = ' '; // fill air
                }
            }
        }

        return makeString(array, y, x);
    }

    private String makeString(char[][] arr, int y, int x){
        StringBuilder sb = new StringBuilder();
        for (int j = y - 1; j >= 0; j--) { //flip
            for (int i = 0; i < x; i++) {
                sb.append(arr[j][i]);
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
