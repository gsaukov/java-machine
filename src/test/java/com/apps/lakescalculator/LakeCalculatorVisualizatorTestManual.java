package com.apps.lakescalculator;

import org.testng.annotations.Test;

import java.util.List;

public class LakeCalculatorVisualizatorTestManual {

    @Test// its not a test just makes sure that visualizator wont blow up due to array out of bounds and lets see the output in console.
    public void console_lake_visualization() {
        List<Lake> lakes = LakesCalculator.calculate(new int[] {3, 3, 2, 2, 1, 0, 0, 1, 5, 5});
        System.out.println(new LakeVisualizator().visualize(lakes.get(0)));
    }

}
