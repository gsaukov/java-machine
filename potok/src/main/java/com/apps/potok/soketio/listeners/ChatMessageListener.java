package com.apps.potok.soketio.listeners;

import com.apps.potok.soketio.model.LogFile;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageListener implements DataListener<LogFile> {

    @Autowired
    private SocketIOServer server;

    @Override
    public void onData(SocketIOClient client, LogFile data, AckRequest ackRequest) {
        server.getClient(client.getSessionId()).sendEvent("message", data);
    }
}
