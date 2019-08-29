package com.apps.cloud.justitia.rest.controller.user.response;

import com.apps.cloud.common.rest.RestResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RestListUsersResponse implements RestResponse {

    private List<String> usernames;

    private RestListUsersResponse() {
        // REST
    }

    private RestListUsersResponse(Builder builder) {
        usernames = builder.usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public static final class Builder {

        private List<String> usernames = new ArrayList<>();

        public Builder withUsername(String username) {
            this.usernames.add(username);
            return this;
        }

        public Builder withUsernames(Collection<String> usernames) {
            this.usernames.addAll(usernames);
            return this;
        }

        public RestListUsersResponse build() {
            return new RestListUsersResponse(this);
        }

    }

}
