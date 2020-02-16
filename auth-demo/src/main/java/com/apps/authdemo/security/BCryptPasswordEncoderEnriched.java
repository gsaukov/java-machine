package com.apps.authdemo.security;

import com.apps.authdemo.socketio.SetSourceListener;
import com.apps.authdemo.socketio.model.FilterMessage;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderEnriched extends BCryptPasswordEncoder {

    private SocketIOServer server;

    public BCryptPasswordEncoderEnriched(SocketIOServer server) {
        this.server = server;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String message = "Matching user password: " +  rawPassword + " with Database hash " + encodedPassword;
        server.getBroadcastOperations().sendEvent("filterMessage", new FilterMessage(SetSourceListener.SOURCE, "BCryptPasswordEncoder", message));
        return super.matches(rawPassword.toString(), encodedPassword);
    }

}
