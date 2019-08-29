package com.apps.cloud.justitia.rest.controller.approval.request;

public class RestRevokeApprovalRequest {

    private String clientId;
    private String userId;
    private String scope;

    private RestRevokeApprovalRequest() {
        // REST
    }

    private RestRevokeApprovalRequest(Builder builder) {
        clientId = builder.clientId;
        userId = builder.userId;
        scope = builder.scope;
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

    public static final class Builder {

        private String clientId;
        private String userId;
        private String scope;

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

        public RestRevokeApprovalRequest build() {
            return new RestRevokeApprovalRequest(this);
        }

    }

}
