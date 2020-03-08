package com.apps.searchandpagination.security.method;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AppAuthorityService {

    public static final String DOMAINS = "domains";
    public static final String RIGHTS = "rights";

    public boolean hasPrivilege(Authentication auth, String targetType, String permission) {
        String authPermission = null;
        Map<String, Object> attributes = getAttributes(auth);
        if(attributes != null && !attributes.isEmpty()){
            HashMap<String, String> rights = (HashMap<String, String>)attributes.get(RIGHTS);
            if(rights != null && !rights.isEmpty()) {
                authPermission = rights.get(targetType);
            }
        }
        return authPermission != null && authPermission.equals(permission);
    }

    public ArrayList<String> getAvailableDomains(Authentication auth){
        ArrayList<String> availableDomains = new ArrayList<>();
        Map<String, Object> attributes = getAttributes(auth);
        if(attributes != null && !attributes.isEmpty()){
            ArrayList<String> domains = (ArrayList<String>)attributes.get(DOMAINS);
            if(domains != null && !domains.isEmpty()) {
                availableDomains.addAll(domains);
            }
        }
        return availableDomains;
    }

    private Map<String, Object> getAttributes(Authentication auth) {
        OAuth2Authentication oAuth2Auth = (OAuth2Authentication) auth;
        DefaultOAuth2User user = ((DefaultOAuth2User)oAuth2Auth.getUserAuthentication().getPrincipal());
        return user.getAttributes();
    }
}
