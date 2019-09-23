package com.apps.cloud.zuul.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//This class intendeded to avoid DefaultUserAuthenticationConverter behaviour to split authorities by comma.
public class AppUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Object principal = map.get(USERNAME);
            return new UsernamePasswordAuthenticationToken(principal, "N/A", getAuthorities(map));
        }
        return null;
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
