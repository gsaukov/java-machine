package com.apps.cloud.justitia.security;

import com.apps.cloud.common.token.AppOAuth2Request;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

//Intended to create extended AppOAuth2Request not used.
public class AppOAuth2RequestFactory extends DefaultOAuth2RequestFactory {

    UserDetailsService userDetailsService;

    public AppOAuth2RequestFactory(ClientDetailsService clientDetailsService, UserDetailsService userDetailsService) {
        super(clientDetailsService);
        this.userDetailsService = userDetailsService;
    }

    @Override
    public OAuth2Request createOAuth2Request(AuthorizationRequest request) {
        return new AppOAuth2Request(request.createOAuth2Request(), null, null);
    }

}
