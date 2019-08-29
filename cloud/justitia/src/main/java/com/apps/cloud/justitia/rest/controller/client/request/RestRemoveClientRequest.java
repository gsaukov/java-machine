package com.apps.cloud.justitia.rest.controller.client.request;

public class RestRemoveClientRequest {

    private String clientId;

    private RestRemoveClientRequest() {
        // REST
    }

    private RestRemoveClientRequest(Builder builder) {
        clientId = builder.clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public static final class Builder {

        private String clientId;

        public Builder withClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public RestRemoveClientRequest build() {
            return new RestRemoveClientRequest(this);
        }

    }

}
