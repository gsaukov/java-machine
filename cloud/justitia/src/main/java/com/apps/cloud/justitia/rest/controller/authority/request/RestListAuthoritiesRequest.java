package com.apps.cloud.justitia.rest.controller.authority.request;

public class RestListAuthoritiesRequest {

    private String username;

    private RestListAuthoritiesRequest() {
        // REST
    }

    private RestListAuthoritiesRequest(Builder builder) {
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

        public RestListAuthoritiesRequest build() {
            return new RestListAuthoritiesRequest(this);
        }

    }

}
