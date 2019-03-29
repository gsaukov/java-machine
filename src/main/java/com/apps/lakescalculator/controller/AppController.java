package com.apps.lakescalculator.controller;

import com.apps.lakescalculator.core.Lake;
import com.apps.lakescalculator.services.LakeCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AppController {

    private InputParser parser;
    private LakeCalculatorService service;

    @GetMapping({"/"})
    public String home(Model model) {
        return "lakecalculator";
    }

    @PostMapping("/")
    public String calculator(@RequestParam("surface") String surface, Model model) {
        try {// cool error handling.
            List<Lake> lakes = service.calculate(parser.parse(surface));
            model.addAttribute("lakes", lakes);
        } catch (Exception e) {
            return "lakecalculator";
        }
        return "lakecalculator";
    }

    @GetMapping("/{id}")
    public String visualizator(@PathVariable("id") String id, Model model) {
        try {// cool error handling.
            model.addAttribute("visualization", service.visualize(id));
        } catch (Exception e) {
            return "lakevisualization";
        }
        return "lakevisualization";
    }

    @Autowired
    public void setParser(InputParser parser) {
        this.parser = parser;
    }

    @Autowired
    public void setService(LakeCalculatorService service) {
        this.service = service;
    }
}
