package com.apps.authdemo.security;

import com.apps.authdemo.socketio.model.BasicAuthMessage;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;

public class BCryptPasswordEncoderEnriched extends BCryptPasswordEncoder {

    private SocketIOServer server;

    public BCryptPasswordEncoderEnriched(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String message = "Matching user password: " +  rawPassword + " with Database hash " + encodedPassword;
        server.getBroadcastOperations().sendEvent("basicAuthFilter", new BasicAuthMessage(message));
        return super.matches(rawPassword.toString(), encodedPassword);
    }

}
