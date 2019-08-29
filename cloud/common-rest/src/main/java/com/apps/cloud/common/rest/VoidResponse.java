package com.apps.cloud.common.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class VoidResponse implements RestResponse {

    public static ResponseEntity<RestResponse> created() {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public static ResponseEntity<RestResponse> ok() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
