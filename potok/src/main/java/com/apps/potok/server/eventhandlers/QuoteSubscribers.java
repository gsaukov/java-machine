package com.apps.potok.server.eventhandlers;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class QuoteSubscribers {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, String>> quoteSubscribersContainer;

    public QuoteSubscribers() {
        this.quoteSubscribersContainer = new ConcurrentHashMap<> ();
    }

    public void addSubscriber(String symbol, String clientId) {
        ConcurrentHashMap<String, String> subscriber = new ConcurrentHashMap<>();
        subscriber.put(clientId, symbol);
        ConcurrentHashMap<String, String> subscribers = quoteSubscribersContainer.putIfAbsent(symbol, subscriber);
        if(subscribers != null){
            subscribers.putIfAbsent(clientId, symbol);
        }
    }

    public ConcurrentHashMap<String, String> getSubscribers(String symbol) {
        return quoteSubscribersContainer.get(symbol);
    }

    public void removeSubscriber(String symbol, String clientId) {
        ConcurrentHashMap<String, String> subscribers = quoteSubscribersContainer.get(symbol);
        if(subscribers != null){
            subscribers.remove(clientId);
        }
    }
}
