package com.apps.authdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthCodeController {

    @Autowired
    private FacebookConnectionFactory connectionFactory;

//    https://localhost:8097/codeauth?code=a
//    https://www.facebook.com/v5.0/dialog/oauth?client_id=2930956966914426&redirect_uri=https%3A%2F%2Flocalhost%3A8097%2Fcodeauth&response_type=token
//    https://www.facebook.com/v5.0/dialog/oauth?client_id=2930956966914426&redirect_uri=https%3A%2F%2Flocalhost%3A8097%2Fcodeauth&response_type=code
    @GetMapping("/codeauth")
    public String codeauth( @RequestParam("code") String code) {
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code, "https://localhost:8097/codeauth", null); // not that the application was deployed at "http://localhost:8080/testApp"

        Facebook facebook = new FacebookTemplate(accessGrant.getAccessToken(), "http://ogp.me/ns/fb#", "2930956966914426");
        return "";
    }

}
