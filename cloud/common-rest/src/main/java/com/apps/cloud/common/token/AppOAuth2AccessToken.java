package com.apps.cloud.common.token;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class AppOAuth2AccessToken implements OAuth2AccessToken {

    private final OAuth2AccessToken accessToken;
    private final Map<String, String> openId;
    private final List<String> domains;
    private final Map<String, String> rights;

    public AppOAuth2AccessToken(OAuth2AccessToken accessToken, Map<String, String> openId, List<String> domains, Map<String, String> rights) {
        this.accessToken = accessToken;
        this.openId = openId;
        this.domains = domains;
        this.rights = rights;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        Map<String, Object> res = new HashMap<>();
        res.putAll(accessToken.getAdditionalInformation());
        res.put("openid", openId);
        res.put("domains", domains);
        res.put("rights", rights);
        return res;
    }

    @Override
    public Set<String> getScope() {
        return accessToken.getScope();
    }

    @Override
    public OAuth2RefreshToken getRefreshToken() {
        return accessToken.getRefreshToken();
    }

    @Override
    public String getTokenType() {
        return accessToken.getTokenType();
    }

    @Override
    public boolean isExpired() {
        return accessToken.isExpired();
    }

    @Override
    public Date getExpiration() {
        return accessToken.getExpiration();
    }

    @Override
    public int getExpiresIn() {
        return accessToken.getExpiresIn();
    }

    @Override
    public String getValue() {
        return accessToken.getValue();
    }
}
