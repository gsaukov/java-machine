package com.apps.lakescalculator.controller;

import com.apps.lakescalculator.core.Lake;
import com.apps.lakescalculator.core.LakeVisualizator;
import com.apps.lakescalculator.core.LakesCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {

    private InputParser parser;
    private LakesCalculator calculator;
    private LakeVisualizator visualizator;

    @GetMapping({"/"})
    public String homePage(Model model) {

        return "lakecalculator";
    }

    @PostMapping("/")
    public String newEntry(@RequestParam("surface") String surface, @RequestParam(name="visualization", defaultValue="false") boolean visualization) {
        try{// cool error handling.

            List<Lake> lakes = calculator.calculate(parser.parse(surface));
            List<String> visualizations = new ArrayList<>();
            if(visualization){
                for(Lake lake : lakes){
                    visualizations.add(visualizator.visualize(lake));
                }
            }
            System.out.println();
        } catch (Exception e){

        }
        return "lakecalculator";
    }

    @Autowired
    public void setParser(InputParser parser) {
        this.parser = parser;
    }

    @Autowired
    public void setCalculator(LakesCalculator calculator) {
        this.calculator = calculator;
    }

    @Autowired
    public void setVisualizator(LakeVisualizator visualizator) {
        this.visualizator = visualizator;
    }
}
