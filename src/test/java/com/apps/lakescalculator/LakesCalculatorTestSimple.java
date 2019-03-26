package com.apps.lakescalculator;

import org.testng.annotations.Test;

import static com.apps.lakescalculator.LakesCalculator.calculate;
import static org.testng.AssertJUnit.assertEquals;

public class LakesCalculatorTestSimple {

    @Test
    public void simple_4_lakes() {
        assertEquals(4, calculate(new int[] {2, 1, 2, 3, 4, 3, 3, 5, 5, 2, 3, 2, 7, 3, 5}).size());
    }

    @Test
    public void simple_0_mountain() {
        assertEquals(0, calculate(new int[]{0, 1, 2, 4, 5, 5, 5, 6, 6, 5, 2, 1}).size());
    }

    @Test
    public void simple_0_flat() {
        assertEquals(0, calculate(new int[]{1, 1, 1, 1, 1, 1, 1, 1}).size());
    }

    @Test
    public void simple_0_left_angle() {
        assertEquals(0, calculate(new int[]{8, 7, 3, 1}).size());
    }

    @Test
    public void simple_0_right_angle() {
        assertEquals(0, calculate(new int[]{0, 4, 5, 8}).size());
    }

    @Test
    public void simple_0_dry_lake_downhill() {
        assertEquals(0, calculate(new int[]{2, 1, 1}).size());
    }
}