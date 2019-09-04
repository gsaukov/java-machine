package com.apps.cloud.justitia.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class AppAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Autowired
    private AppClientDetailsService appClientDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
            super.onAuthenticationSuccess(request, response, authentication);
//        authenticationManager.authenticate(authentication);
    }

}
