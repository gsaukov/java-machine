package com.apps.authdemo.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @ResponseBody
    @GetMapping("/login")
    public String basicAuth() {
        return "WELCOME BACK " + getRole();
    }

    @ResponseBody
    @PostMapping("/formlogin")
    public String formAuth() {
        return "WELCOME BACK " + getRole();
    }

    private String getRole () {
        String role = "";
        for (GrantedAuthority grantedAuth : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            role = grantedAuth.getAuthority();
        }
        return role;
    }

}