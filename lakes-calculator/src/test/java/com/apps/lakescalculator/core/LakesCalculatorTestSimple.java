package com.apps.lakescalculator.core;

import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class LakesCalculatorTestSimple {

    private LakesCalculator calculator = new LakesCalculator();
    private LakeVisualizator visualizator = new LakeVisualizator();

    @Test
    public void simple_3_same_lakes() {
//        InputParser parser = new InputParser();
//        parser.parse("1222,2,1,1");
        assertEquals(3, calculateNumberOfLakes(new int[] {2, 1, 2, 1, 2, 2, 1, 2}));
    }

    @Test
    public void simple_4_lakes() {                    //              1           1  1        5  2  3     2
        assertEquals(4, calculateNumberOfLakes(new int[] {2, 1, 2, 3, 4, 3, 3, 5, 5, 0, 3, 2, 7, 3, 5}));
    }

    @Test
    public void simple_0_mountain() {
        assertEquals(0, calculateNumberOfLakes(new int[]{0, 1, 2, 4, 5, 3, 2, 1, 0}));
    }

    @Test
    public void simple_0_volcano() {
        assertEquals(0, calculateNumberOfLakes(new int[]{0, 1, 2, 4, 5, 5, 5, 6, 6, 5, 2, 1}));
    }

    @Test
    public void simple_1_small_mountain_between_two() {
        assertEquals(1, calculateNumberOfLakes(new int[]{0, 1, 2, 5, 6, 3, 5, 3, 6, 5, 2, 1}));
    }


    @Test
    public void simple_0_flat() {
        assertEquals(0, calculateNumberOfLakes(new int[]{1, 1, 1, 1, 1, 1, 1, 1}));
    }

    @Test
    public void simple_0_left_angle() {
        assertEquals(0, calculateNumberOfLakes(new int[]{8, 7, 3, 1}));
    }

    @Test
    public void simple_0_right_angle() {
        assertEquals(0, calculateNumberOfLakes(new int[]{0, 4, 5, 8}));
    }

    @Test
    public void simple_0_dry_lake_downhill() {
        assertEquals(0, calculateNumberOfLakes(new int[]{3, 2, 2, 1}));
    }

    private int calculateNumberOfLakes(int[] arr) {
        List<Lake> lakes = calculator.calculate(arr);
        for(Lake lake : lakes){
            System.out.println(visualizator.visualize(lake));
        }
        return lakes.size() == 0 ? 0 : lakes.size() - 1; //getting rid of total lake
    }
}