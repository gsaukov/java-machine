package com.apps.cloud.common.token;

import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.List;

public class AppOAuth2Request extends OAuth2Request {

    private final List<String> domains;

    public AppOAuth2Request(OAuth2Request other, List<String> domains) {
        super(other);
        this.domains = domains;
    }

    public List<String> getDomains() {
        return domains;
    }
}
