package com.apps.cloud.justitia.rest.controller.login.response;

import com.apps.cloud.common.rest.RestResponse;

import java.util.Map;
import java.util.Set;

public class RestLoginResponse implements RestResponse {

    private String reference;
    private Set<String> challenges;
    private Map<String, String> providedParams;

    private RestLoginResponse() {
        // REST
    }

    private RestLoginResponse(Builder builder) {
        reference = builder.reference;
        challenges = builder.challenges;
        providedParams = builder.providedParams;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Set<String> getChallenges() {
        return challenges;
    }

    public void setChallenges(Set<String> challenges) {
        this.challenges = challenges;
    }

    public Map<String, String> getProvidedParams() {
        return providedParams;
    }

    public void setProvidedParams(Map<String, String> providedParams) {
        this.providedParams = providedParams;
    }

    public static final class Builder {

        private String reference;
        private Set<String> challenges;
        private Map<String, String> providedParams;

        public Builder withReference(String reference) {
            this.reference = reference;
            return this;
        }

        public Builder withChallenges(Set<String> challenges) {
            this.challenges = challenges;
            return this;
        }

        public Builder withProvidedParams(Map<String, String> providedParams) {
            this.providedParams = providedParams;
            return this;
        }

        public RestLoginResponse build() {
            return new RestLoginResponse(this);
        }

    }

}
