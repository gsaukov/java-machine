package com.apps.authdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthCodeController {

    static String[] PROFILE_FIELDS = {
            "id", "about", "age_range", "birthday", "cover", "currency", "devices", "education", "email",
            "favorite_athletes", "favorite_teams", "first_name", "gender", "hometown", "inspirational_people", "installed", "install_type",
            "is_verified", "languages", "last_name", "link", "locale", "location", "meeting_for", "middle_name", "name", "name_format",
            "political", "quotes", "payment_pricepoints", "relationship_status", "religion", "security_settings", "significant_other",
            "sports", "test_group", "timezone", "third_party_id", "updated_time", "verified", "video_upload_limits", "viewer_can_send_gift",
            "website", "work"
    };

    @Autowired
    private FacebookConnectionFactory connectionFactory;

//    https://localhost:8097/codeauth?code=a
//    https://www.facebook.com/v5.0/dialog/oauth?client_id=2930956966914426&redirect_uri=https%3A%2F%2Flocalhost%3A8097%2Fcodeauth&response_type=token
//    https://www.facebook.com/v5.0/dialog/oauth?client_id=2930956966914426&redirect_uri=https%3A%2F%2Flocalhost%3A8097%2Fcodeauth&response_type=code

    @ResponseBody
    @GetMapping("/codeauth")
    public User codeauth( @RequestParam("code") String code) {
        AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(code, "https://localhost:8097/codeauth", null); // not that the application was deployed at "http://localhost:8080/testApp"

        Facebook facebook = new FacebookTemplate(accessGrant.getAccessToken(), "http://ogp.me/ns/fb#", "2930956966914426");

        User user = facebook.fetchObject("me", User.class, PROFILE_FIELDS);
        return user;
    }

}
