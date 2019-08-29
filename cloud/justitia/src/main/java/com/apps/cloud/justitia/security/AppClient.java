package com.apps.cloud.justitia.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.springframework.util.StringUtils.commaDelimitedListToSet;

public class AppClient implements ClientDetails {

    private String clientId;
    private String clientSecret;
    private Set<String> authorizedGrantTypes;
    private Set<String> scope;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private Boolean autoApprove;
    private Set<String> registeredRedirectUris;

    private AppClient() {
        // JPA
    }

    private AppClient(Builder builder) {
        clientId = builder.clientId;
        clientSecret = builder.clientSecret;
        authorizedGrantTypes = commaDelimitedListToSet(builder.grantTypes);
        scope = commaDelimitedListToSet(builder.scopes);
        accessTokenValiditySeconds = builder.accessTokenDuration;
        refreshTokenValiditySeconds = builder.refreshTokenDuration;
        autoApprove = builder.autoApprove;
        registeredRedirectUris = commaDelimitedListToSet(builder.redirectUri);
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return scope;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUris;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return emptySet();
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return autoApprove;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return emptyMap();
    }

    @Override
    public Set<String> getResourceIds() {
        return emptySet();
    }

    public static final class Builder {

        private String clientId;
        private String clientSecret;
        private String grantTypes;
        private String scopes;
        private Integer accessTokenDuration;
        private Integer refreshTokenDuration;
        private Boolean autoApprove;
        private String redirectUri;

        public Builder withClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder withClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder withGrantTypes(String grantTypes) {
            this.grantTypes = grantTypes;
            return this;
        }

        public Builder withScopes(String scopes) {
            this.scopes = scopes;
            return this;
        }

        public Builder withAccessTokenDuration(Integer accessTokenDuration) {
            this.accessTokenDuration = accessTokenDuration;
            return this;
        }

        public Builder withRefreshTokenDuration(Integer refreshTokenDuration) {
            this.refreshTokenDuration = refreshTokenDuration;
            return this;
        }

        public Builder withAutoApprove(Boolean autoApprove) {
            this.autoApprove = autoApprove;
            return this;
        }

        public Builder withRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }

        public AppClient build() {
            return new AppClient(this);
        }

    }

}

