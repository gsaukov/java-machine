package com.apps.authdemo.controller;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthCodeController {

//    https://localhost:8097/codeauth?code=a
//    https://www.facebook.com/v5.0/dialog/oauth?client_id=2930956966914426&redirect_uri=https%3A%2F%2Flocalhost%3A8097%2Fcodeauth&response_type=token
    @GetMapping("/codeauth")
    public String codeauth( @RequestParam("#access_token") String code) {
        Facebook facebook = new FacebookTemplate(code);
        return "";
    }

}
