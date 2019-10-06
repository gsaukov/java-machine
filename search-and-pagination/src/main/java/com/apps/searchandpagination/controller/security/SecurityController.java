package com.apps.searchandpagination.controller.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping({"/performlogout"})
    public String performLogout() {
        return "logedout";
    }

    @GetMapping({"logedout"})
    public String logedout() {
        return "/logedout";
    }

}
