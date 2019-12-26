package com.apps.potok.exchange.core;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

@Service
//Buy orders
public class AskContainer {

    private final ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>>> askContainer;

    public AskContainer() {
        this.askContainer = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>>> get() {
        return askContainer;
    }

    public ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> get(String symbol) {
        return askContainer.get(symbol);
    }

    public void put(String symbol, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> symbolContainer) {
        askContainer.put(symbol, symbolContainer);
    }

    public boolean containsKey(String symbolName) {
        return askContainer.containsKey(symbolName);
    }

    public boolean removeAsk(Order order) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> symbolOrderContainer = askContainer.get(order.getSymbol());
        ConcurrentLinkedDeque<Order> accountContainer = symbolOrderContainer.get(order.getVal());
        return accountContainer.remove(order);
    }

    public void insertAsk(Order order) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> symbolOrderContainer = askContainer.get(order.getSymbol());
        insertPrice(symbolOrderContainer, order);
    }

    private void insertPrice(ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> symbolOrderContainer, Order order) {
        final ConcurrentLinkedDeque<Order> accountContainer  = new ConcurrentLinkedDeque();
        final ConcurrentLinkedDeque<Order> existingAccountContainer = symbolOrderContainer.putIfAbsent(order.getVal(), accountContainer);
        if(existingAccountContainer == null){
            accountContainer.offer(order);
        } else {
            existingAccountContainer.offer(order);
        }
    }
}
