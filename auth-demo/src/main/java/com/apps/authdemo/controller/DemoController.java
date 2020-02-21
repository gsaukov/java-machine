package com.apps.authdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DemoController {
    @GetMapping("/authdemo/partone")
    public String partOneController(Model model) {
        return "partone";
    }

    @GetMapping("/authdemo/parttwo")
    public String partTwoController(Model model) {
        return "parttwo";
    }

    @GetMapping({"/authdemo/partone/authform"})
    public String getPartOneAuthForm(Model model) {
        return "authform :: authform";
    }
}
