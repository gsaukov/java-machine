package com.apps.authdemo.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SessionDisconnectListener implements DisconnectListener {

    @Override
    public void onDisconnect(SocketIOClient client) {

    }

}
