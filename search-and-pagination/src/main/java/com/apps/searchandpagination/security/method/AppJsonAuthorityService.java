package com.apps.searchandpagination.security.method;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

//@Service
@Deprecated //Use AppAuthorityService
public class AppJsonAuthorityService {

    public static final String JSON_AUTHORITY = "JSON_AUTHORITY";

    public static final String DOMAIN = "domain";

    public boolean hasPrivilege(Authentication auth, String targetType, String permission) {
        String authPermission = null;
        String jsonAuth = getJsonAuthority(auth);
        if(jsonAuth != null && !jsonAuth.isEmpty()){
            authPermission = getFromJsonObject(jsonAuth, targetType);
        }
        return authPermission != null && authPermission.equals(permission);
    }

    public ArrayList<String> getAvailableDomains(Authentication auth){
        ArrayList<String> availibleDomains = new ArrayList<>();
        String jsonAuth = getJsonAuthority(auth);
        if(jsonAuth != null && !jsonAuth.isEmpty()){
            availibleDomains.addAll(getFromJsonArray(jsonAuth, DOMAIN));
        }
        return availibleDomains;
    }

    private String getJsonAuthority(Authentication auth){
        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
            if (grantedAuth.getAuthority().startsWith(JSON_AUTHORITY)) {
                return grantedAuth.getAuthority().substring(JSON_AUTHORITY.length());
            }
        }
        return null;
    }

    private ArrayList<String> getFromJsonArray(String jsonAuth, String key) {
        ArrayList<String> jsonArray = new ArrayList<>();
        try {
            JSONObject j = new JSONObject(jsonAuth);
            if (j.has(key)) {
                JSONArray jArray = j.getJSONArray(key);
                for (int i = 0; i < jArray.length(); i++) {
                    jsonArray.add(jArray.getString(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private String getFromJsonObject(String jsonAuth, String key) {
        String jsonString = null;
        try {
            JSONObject j = new JSONObject(jsonAuth);
            if (j.has(key)) {
                jsonString = j.get(key).toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
