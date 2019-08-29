package com.apps.cloud.justitia.security;

import com.apps.cloud.justitia.data.entity.AccessToken;
import com.apps.cloud.justitia.data.repository.AccessTokenRepository;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Map;

public class CustomJwtTokenStore extends JwtTokenStore {

    private AccessTokenRepository accessTokenRepository;

    public CustomJwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer, AccessTokenRepository accessTokenRepository) {
        super(jwtTokenEnhancer);
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        super.storeAccessToken(token, authentication);

        Map<String, Object> info = token.getAdditionalInformation();

        accessTokenRepository.save(new AccessToken.Builder()
                .withJti(info.get("jti").toString())
                .withEncoded(token.getValue())
                .build());
    }

}
