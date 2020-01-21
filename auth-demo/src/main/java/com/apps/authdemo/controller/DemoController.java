package com.apps.authdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

        @GetMapping("/part1")
        public String partOneController(Model model) {
            return "part1";
        }
}
