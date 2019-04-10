package com.apps.lakescalculator.controller;

import com.apps.lakescalculator.core.Lake;
import com.apps.lakescalculator.services.LakeCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<Lake> lakes = service.calculate(parser.parse(surface));
        model.addAttribute("lakes", lakes);
        return "lakecalculator";
    }

    @GetMapping("/view/{id}")
    public String visualizator(@PathVariable("id") String id, Model model) {
        model.addAttribute("visualization", service.visualize(id));
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
