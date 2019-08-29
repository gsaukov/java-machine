package com.apps.cloud.justitia.rest.controller.user.request;

public class RestRemoveUserRequest {

    private String username;

    private RestRemoveUserRequest() {
        // REST
    }

    private RestRemoveUserRequest(Builder builder) {
        username = builder.username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static final class Builder {

        private String username;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public RestRemoveUserRequest build() {
            return new RestRemoveUserRequest(this);
        }

    }

}
