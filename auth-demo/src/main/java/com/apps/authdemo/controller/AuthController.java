package com.apps.authdemo.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        CsrfToken csrfToken = (CsrfToken)request.getAttribute(CsrfToken.class.getName());
        return "WELCOME BACK " + getRole() + "here is your new CSRF token [" + csrfToken.getToken() + "]" ;
    }

    public static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(false);
    }

    private String getRole () {
        String role = "";
        for (GrantedAuthority grantedAuth : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            role = grantedAuth.getAuthority();
        }
        return role;
    }

}