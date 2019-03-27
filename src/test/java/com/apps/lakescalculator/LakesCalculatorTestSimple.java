package com.apps.lakescalculator;

import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class LakesCalculatorTestSimple {

    @Test
    public void simple_3_same_lakes() {
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
        List<Lake> lakes = LakesCalculator.calculate(arr);
        return lakes.size() == 0 ? 0 : lakes.size() - 1; //getting rid of total lake
    }
}