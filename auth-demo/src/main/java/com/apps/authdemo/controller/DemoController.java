package com.apps.authdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {
    @GetMapping("/authdemo/partone")
    public String partOneController(Model model) {
        return "partone";
    }
}
