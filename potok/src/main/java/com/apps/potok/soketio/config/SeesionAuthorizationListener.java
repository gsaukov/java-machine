package com.apps.potok.soketio.config;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class SeesionAuthorizationListener implements AuthorizationListener {

    @Value("${session.test-mode-authentication}")
    private boolean testModeAuthentication;

    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    @Override
    public boolean isAuthorized(HandshakeData data) {
        if(testModeAuthentication) {
            return true;
        }

        String cookie = data.getSingleUrlParam("SESSIONID");
        if(cookie != null) {
            String sessionId = new String(Base64.getDecoder().decode(cookie));
            Session session = sessionRepository.findById(sessionId);
            SecurityContext securityContext = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)securityContext.getAuthentication();
            return oAuth2Authentication.isAuthenticated();
        } else {
            return false;
        }
    }
}
