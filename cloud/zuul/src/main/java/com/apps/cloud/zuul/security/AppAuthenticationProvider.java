package com.apps.cloud.zuul.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

public class AppAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        AppAuthenticationToken authenticationToken = (AppAuthenticationToken) authentication;

        if(authenticationToken.getToken() == null){
            throw new SessionAuthenticationException("Token not found.");
        }

        authenticationToken.setAuthenticated(true);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (AppAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
