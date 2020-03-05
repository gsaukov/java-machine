package com.apps.cloud.justitia.security;

import com.apps.cloud.common.token.AppOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class AppOAuth2AccessTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        AppUser appUser = (AppUser)authentication.getPrincipal();
        return new AppOAuth2AccessToken(accessToken, appUser.getDomains(), appUser.getRights());
    }
}
