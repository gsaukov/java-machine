package com.apps.potok.server.eventhandlers;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class QuoteSubscribers {

    private final ConcurrentHashMap<String, ConcurrentHashMap<UUID, String>> quoteSubscribersContainer;

    public QuoteSubscribers() {
        this.quoteSubscribersContainer = new ConcurrentHashMap<> ();
    }

    public void addSubscriber(String symbol, UUID clientId) {
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

    public void removeSubscriber(String symbol, UUID clientId) {
        ConcurrentHashMap<UUID, String> subscribers = quoteSubscribersContainer.get(symbol);
        if(subscribers != null){
            subscribers.remove(clientId);
        }
    }
}
