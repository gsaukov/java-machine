package com.apps.cloud.zuul.rest.filter;

import com.apps.cloud.zuul.rest.client.OAuth2Client;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.slf4j.LoggerFactory.getLogger;

@Component
public class AuthorizationFilterSupport {

    private static final Logger logger = getLogger(AuthorizationFilterSupport.class);

    private static final ObjectMapper objectMapper = new ObjectMapper(); // Thread Safe

    @Autowired
    private OAuth2Client oAuth2Client;

    @Value("${feign.oauth2.client-id}")
    private String clientId;

    public Optional<String> getToken(String basicAuthorization) {
        byte[] decodedBytes = Base64.getDecoder().decode(basicAuthorization);
        String[] decodedValue = new String(decodedBytes).split(":");

        String username = decodedValue[0];
        String password = decodedValue[1];

        return getToken(username, password);
    }

    private Optional<String> getToken(String username, String password) {
        try {
            return getTokenFromAuthServer(username, password);
        } catch (Exception exc) {
            logger.warn("Failed to exchange basic with bearer for user {} due to {}.", username, exc.getMessage());

            return empty();
        }
    }

    private Optional<String> getTokenFromAuthServer(String username, String password) throws IOException {
        String json = oAuth2Client.getToken("password", clientId, toFormParams(username, password));

        JsonNode jsonNode = objectMapper.readTree(json);

        return of(jsonNode.get("access_token").textValue());
    }

    private Map<String, ?> toFormParams(String username, String password) {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("username", username);
        formParams.put("password", password);
        return formParams;
    }

}
