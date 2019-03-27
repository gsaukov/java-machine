package com.apps.lakescalculator.controller;

import com.apps.lakescalculator.core.LakeVisualizator;
import com.apps.lakescalculator.core.LakesCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AppController {

    private LakesCalculator calculator;
    private LakeVisualizator visualizator;

    @GetMapping({"/"})
    public String homePage(Model model) {

        return "lakecalculator";
    }

    @PostMapping("/")
    public String newEntry(@RequestParam("surface") String surface) {
        try{// cool error handling.
           System.out.println(surface);
        } catch (Exception e){

        }
        return "lakecalculator";
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
