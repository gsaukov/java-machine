package com.apps.authdemo.socketio;

import com.corundumstudio.socketio.SocketIOServer;

public class SocketIoProxy {

    private SocketIOServer server;

    public SocketIoProxy(SocketIOServer server) {
        this.server = server;
    }

    public void sendSocketIoMessage (String type, Object message) {
        server.getBroadcastOperations().sendEvent(type, message);
    }
}
