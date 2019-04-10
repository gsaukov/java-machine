package com.apps.bowlingcalculator.controller;

import com.apps.bowlingcalculator.core.BowlingCalculator;
import com.apps.bowlingcalculator.core.exception.BowlingRulesViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {

    private BowlingCalculator bowlingCalculator;

    private ModelHandler modelHandler;

    @GetMapping("/")
    public String homePage(Model model) {
        modelHandler.initializeEmptyModel(model);
        return "bowlingcalculator";
    }

    @GetMapping("/reset")
    public String reset(Model model) {
        bowlingCalculator.reset();
        modelHandler.initializeEmptyModel(model);
        return "bowlingcalculator";
    }

    @PostMapping("/")
    public String scorePage(@RequestParam("rolls") String rolls, Model model) {
        try{
            modelHandler.enrichModel(model, bowlingCalculator.calculate(rolls));
        } catch (BowlingRulesViolationException e){
            bowlingCalculator.reset();
            modelHandler.enrichError(model, e);
        }
        return "bowlingcalculator";
    }

    @Autowired
    public void setBowlingCalculator(BowlingCalculator bowlingCalculator) {
        this.bowlingCalculator = bowlingCalculator;
    }

    @Autowired
    public void setModelHandler(ModelHandler modelHandler) {
        this.modelHandler = modelHandler;
    }

}
