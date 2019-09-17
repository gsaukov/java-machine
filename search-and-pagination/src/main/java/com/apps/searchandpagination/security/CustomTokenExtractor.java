package com.apps.searchandpagination.security;

import com.apps.cloud.common.data.token.AppAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class CustomTokenExtractor implements TokenExtractor {

    @Override
    public Authentication extract(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (supports(auth)) {
            String tokenValue = extractSessionToken(auth);
            PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(tokenValue, "");
            return authentication;
        }
        return null;
    }

    protected String extractSessionToken(Authentication auth) {
        AppAuthenticationToken authenticationToken = (AppAuthenticationToken) auth;
        return authenticationToken.getToken();
    }

    public boolean supports(Authentication authentication) {
        return authentication != null &&  (AppAuthenticationToken.class.isAssignableFrom(authentication.getClass()));
    }
}
