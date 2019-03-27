package com.apps.lakescalculator;

import org.testng.annotations.Test;

import java.util.List;

public class LakeCalculatorVisualizatorTestManual {

    private LakesCalculator calculator = new LakesCalculator();
    private LakeVisualizator visualizator = new LakeVisualizator();

    @Test// its not a test just makes sure that visualizator wont blow up due to array out of bounds and lets see the output in console.
    public void console_lake_visualization() {
        List<Lake> lakes = calculator.calculate(new int[] {5,1,4,1,7});
        System.out.println(visualizator.visualize(lakes.get(0)));
    }

}
