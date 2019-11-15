package com.apps.potok.soketio.config;

import com.corundumstudio.socketio.HandshakeData;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;

import java.util.Base64;

public class SessionUtil {

    public static OAuth2Authentication getOAuth2Authentication(HandshakeData handshakeData, RedisOperationsSessionRepository sessionRepository){
        String cookie = handshakeData.getSingleUrlParam("SESSIONID");
        if(cookie != null) {
            String sessionId = new String(Base64.getDecoder().decode(cookie));
            Session session = sessionRepository.findById(sessionId);
            SecurityContext securityContext = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
            return (OAuth2Authentication) securityContext.getAuthentication();
        } else {
            return null;
        }
    }
}
