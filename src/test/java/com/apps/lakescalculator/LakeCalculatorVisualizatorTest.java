package com.apps.lakescalculator;

import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class LakeCalculatorVisualizatorTest {

    @Test
    public void simple_4_lakes() {
        List<Lake> lakes = LakesCalculator.calculate(new int[] {2, 0, 2});
        LakeVisualizator.visualize(lakes.get(0));

    }

}
