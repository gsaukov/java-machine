package com.apps.cloud.common.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class ErrorResponse implements RestResponse {

    private Set<String> errorCodes;

    private ErrorResponse() {
        // REST
    }

    private ErrorResponse(Builder builder) {
        errorCodes = unmodifiableSet(builder.errorCodes);
    }

    public Set<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(Set<String> errorCodes) {
        this.errorCodes = errorCodes;
    }

    public static final class Builder {

        private Set<String> errorCodes = new HashSet<>();

        public Builder withErrorCode(String errorCode) {
            this.errorCodes.add(errorCode);
            return this;
        }

        public Builder withErrorCodes(Collection<String> errorCodes) {
            this.errorCodes.addAll(errorCodes);
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }

    }

    public static ResponseEntity<RestResponse> badRequest(String errorCode) {
        return new ResponseEntity<>(new Builder().withErrorCode(errorCode).build(), HttpStatus.BAD_REQUEST);
    }

}
