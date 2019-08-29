package com.apps.cloud.justitia.rest.controller.approval.response;

public class RestListApprovalDetails {

    private String clientId;
    private String userId;
    private String scope;
    private String expiration;

    private RestListApprovalDetails() {
        // REST
    }

    private RestListApprovalDetails(Builder builder) {
        clientId = builder.clientId;
        scope = builder.scope;
        userId = builder.userId;
        expiration = builder.expiration;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public static final class Builder {

        private String clientId;
        private String userId;
        private String scope;
        private String expiration;

        public Builder withClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withScope(String scope) {
            this.scope = scope;
            return this;
        }

        public Builder withExpiration(String expiration) {
            this.expiration = expiration;
            return this;
        }

        public RestListApprovalDetails build() {
            return new RestListApprovalDetails(this);
        }

    }

}

