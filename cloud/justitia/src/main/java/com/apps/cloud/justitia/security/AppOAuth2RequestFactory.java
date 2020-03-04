package com.apps.cloud.justitia.security;

import com.apps.cloud.common.token.AppOAuth2Request;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

import java.util.List;

public class AppOAuth2RequestFactory extends DefaultOAuth2RequestFactory {

    public AppOAuth2RequestFactory(ClientDetailsService clientDetailsService) {
        super(clientDetailsService);
    }

    @Override
    public AppOAuth2Request createOAuth2Request(ClientDetails client, TokenRequest tokenRequest) {
//        List<String> domains = ((AppClient) client).getAuthorizedGrantTypes();
        return new AppOAuth2Request(super.createOAuth2Request(client, tokenRequest), null);
    }

}
