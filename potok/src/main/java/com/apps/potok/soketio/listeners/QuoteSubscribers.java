package com.apps.potok.soketio.listeners;

import com.corundumstudio.socketio.SocketIOServer;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//@Component
@Deprecated //use QuoteSubscribersV2
public class QuoteSubscribers {

    private SocketIOServer server;
    private final ConcurrentHashMap<String, ConcurrentHashMap<UUID, String>> quoteSubscribersContainer;

    public QuoteSubscribers(SocketIOServer server) {
        this.server = server;
        this.quoteSubscribersContainer = new ConcurrentHashMap<> ();
    }

    public void addSubscriber(String symbol, UUID clientId) {
        server.getClient(clientId).joinRoom(symbol);
        ConcurrentHashMap<UUID, String> subscriber = new ConcurrentHashMap<>();
        subscriber.put(clientId, symbol);
        ConcurrentHashMap<UUID, String> subscribers = quoteSubscribersContainer.putIfAbsent(symbol, subscriber);
        if(subscribers != null){
            subscribers.putIfAbsent(clientId, symbol);
        }
    }

    public ConcurrentHashMap<UUID, String> getSubscribers(String symbol) {
        return quoteSubscribersContainer.get(symbol);
    }

    public void removeSubscriber(UUID clientId) {
        for (ConcurrentHashMap<UUID, String> value : quoteSubscribersContainer.values()){
            value.remove(clientId);
        }
    }
}
