package com.apps.cloud.common.token;

import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.List;
import java.util.Map;

// call to get authcode that further will be exchanged for token.
public class AppOAuth2Request extends OAuth2Request {

    private final List<String> domains;

    private final Map<String, String> rights;

    public AppOAuth2Request(OAuth2Request other, List<String> domains, Map<String, String> rights) {
        super(other);
        this.domains = domains;
        this.rights = rights;
    }

    public List<String> getDomains() {
        return domains;
    }

    public Map<String, String> getRights() {
        return rights;
    }
}
