package com.apps.cloud.justitia.security;

import com.apps.cloud.common.token.AppOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class AppOAuth2AccessTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        AppUser appUser = (AppUser)authentication.getPrincipal();
        enrichOpenId(appUser, accessToken, authentication);
        return new AppOAuth2AccessToken(accessToken, appUser.getOpenId(), appUser.getDomains(), appUser.getRights());
    }

    private void enrichOpenId(AppUser appUser, OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, String> openId =  new HashMap<>();
        if(openId != null) {
            openId.put("iss", "Justitia");
            openId.put("sub", appUser.getUserId());
            openId.put("aud", authentication.getOAuth2Request().getClientId());
            openId.put("exp", Integer.toString(accessToken.getExpiration().getSeconds()));
            openId.put("iat", Long.toString(System.currentTimeMillis()/1000));
        }
        appUser.setOpenId(openId);
    }
}
