package com.apps.potok.soketio.listeners;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class QuoteSubscribersV2 {

    private SocketIOServer server;

    public QuoteSubscribersV2(SocketIOServer server) {
        this.server = server;
    }

    public void addSubscriber(String symbol, UUID clientId) {
        server.getClient(clientId).joinRoom(symbol);
    }

    public void removeSubscriber(UUID clientId) {
        for(String room : server.getClient(clientId).getAllRooms()) {
            server.getClient(clientId).leaveRoom(room);
        }
    }
}
