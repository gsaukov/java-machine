package com.apps.cloud.zuul.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class AppUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    public Authentication extractAuthentication(Map<String, ?> map) {
        Map<String, Object> details = (Map<String, Object>)map;
        DefaultOAuth2User user = new DefaultOAuth2User(getAuthorities(map), details, USERNAME);
        return new OAuth2AuthenticationToken(user,  getAuthorities(map), map.get("client_id").toString());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof ArrayList) {
            ArrayList<String> authoritiesList = (ArrayList)authorities;
            return AuthorityUtils.createAuthorityList(authoritiesList.toArray(new String[authoritiesList.size()]));
        }
        throw new IllegalArgumentException("Authorities must be a Collection");
    }

}
