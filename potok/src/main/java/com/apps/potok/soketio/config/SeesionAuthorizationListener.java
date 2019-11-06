package com.apps.potok.soketio.config;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class SeesionAuthorizationListener implements AuthorizationListener {

    @Autowired
    private RedisOperationsSessionRepository sessionRepository;

    @Override
    public boolean isAuthorized(HandshakeData data) {
        String cookie = data.getSingleUrlParam("SESSIONID");
        if(cookie != null) {
            String sessionId = new String(Base64.getDecoder().decode(cookie));
            sessionRepository.findById(sessionId);
        }
        return true;
    }
}
