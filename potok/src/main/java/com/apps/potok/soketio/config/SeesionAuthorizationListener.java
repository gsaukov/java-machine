package com.apps.potok.soketio.config;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;

public class SeesionAuthorizationListener implements AuthorizationListener {

    @Override
    public boolean isAuthorized(HandshakeData data) {
        data.getSingleUrlParam("SESSIONID");
        return true;
    }
}
