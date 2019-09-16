package com.apps.cloud.common.data.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class AppAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    public AppAuthenticationToken(String token) {
        super(null);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    public String getToken() {
        return token;
    }

}
