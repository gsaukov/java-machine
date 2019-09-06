package com.apps.cloud.zuul.security;

import com.apps.cloud.zuul.rest.client.OAuth2Client;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.of;

public class AppAuthenticationFilter extends
        AbstractAuthenticationProcessingFilter {

    @Value("${feign.oauth2.client-id}")
    private String clientId;

    @Value("${feign.oauth2.client-secret}")
    private String clientSecret;

    @Autowired
    private OAuth2Client oAuth2Client;

    private static final ObjectMapper objectMapper = new ObjectMapper(); // Thread Safe

    public AppAuthenticationFilter() {
        super(new RegexRequestMatcher(".*\\/sdapplication\\?code=.*", "GET"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String code = request.getParameter("code");

        String token = getTokenFromAuthServer(code).get();

        AppAuthenticationToken authRequest = new AppAuthenticationToken(token);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    private Optional<String> getTokenFromAuthServer(String code) throws IOException {
        String json = oAuth2Client.getToken("authorization_code", clientId, toFormParams(code, clientSecret));

        JsonNode jsonNode = objectMapper.readTree(json);

        return of(jsonNode.get("access_token").textValue());
    }

    private Map<String, ?> toFormParams(String code, String clientSecret) {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("code", code);
        formParams.put("client_secret", clientSecret);
        return formParams;
    }

}

