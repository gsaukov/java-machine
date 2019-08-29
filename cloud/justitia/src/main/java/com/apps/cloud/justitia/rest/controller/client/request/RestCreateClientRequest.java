package com.apps.cloud.justitia.rest.controller.client.request;

public class RestCreateClientRequest {

    private String clientId;
    private String clientSecret;
    private String grantTypes;
    private String scopes;
    private Integer accessTokenDuration;
    private Integer refreshTokenDuration;
    private Boolean autoApprove;
    private String redirectUri;

    private RestCreateClientRequest() {
        // REST
    }

    private RestCreateClientRequest(Builder builder) {
        clientId = builder.clientId;
        clientSecret = builder.clientSecret;
        grantTypes = builder.grantTypes;
        scopes = builder.scopes;
        accessTokenDuration = builder.accessTokenDuration;
        refreshTokenDuration = builder.refreshTokenDuration;
        autoApprove = builder.autoApprove;
        redirectUri = builder.redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public Integer getAccessTokenDuration() {
        return accessTokenDuration;
    }

    public void setAccessTokenDuration(Integer accessTokenDuration) {
        this.accessTokenDuration = accessTokenDuration;
    }

    public Integer getRefreshTokenDuration() {
        return refreshTokenDuration;
    }

    public void setRefreshTokenDuration(Integer refreshTokenDuration) {
        this.refreshTokenDuration = refreshTokenDuration;
    }

    public Boolean getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(Boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
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

        public RestCreateClientRequest build() {
            return new RestCreateClientRequest(this);
        }

    }

}

