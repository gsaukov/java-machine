package com.apps.cloud.zuul.rest.client;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient("justitia")
public interface OAuth2Client {

    @RequestMapping(method = POST, value = "/oauth/token", produces = APPLICATION_JSON_VALUE,
            consumes = APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    String getToken(@RequestParam("grant_type") String grantType,
                    @RequestParam("client_id") String clientId,
                    @RequestBody Map<String, ?> formParams);

}