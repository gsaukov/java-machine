package com.apps.cloud.justitia.rest.controller.client.response;

public class RestListClientDetails {

    private String clientId;
    private String grantTypes;
    private String scopes;
    private Integer accessTokenDuration;
    private Integer refreshTokenDuration;
    private Boolean autoApprove;
    private String redirectUri;

    private RestListClientDetails() {
        // REST
    }

    private RestListClientDetails(Builder builder) {
        clientId = builder.clientId;
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

        public RestListClientDetails build() {
            return new RestListClientDetails(this);
        }

    }

}

