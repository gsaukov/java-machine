package com.apps.authdemo.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import org.springframework.stereotype.Service;

@Service
public class SessionConnectListener implements ConnectListener {

    @Override
    public void onConnect(SocketIOClient client) {

    }
}
