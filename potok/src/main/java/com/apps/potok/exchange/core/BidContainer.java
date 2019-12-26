package com.apps.potok.exchange.core;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

@Service
// Sell orders
public class BidContainer {

    private final ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>>> bidContainer;

    public BidContainer() {
        this.bidContainer = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>>> get() {
        return bidContainer;
    }

    public void put(String symbol, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> symbolContainer) {
        bidContainer.put(symbol, symbolContainer);
    }

    public ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> get(String symbol) {
        return bidContainer.get(symbol);
    }

    public boolean containsKey(String symbolName) {
        return bidContainer.containsKey(symbolName);
    }

    public boolean removeBid(Order order) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> symbolOrderContainer = bidContainer.get(order.getSymbol());
        ConcurrentLinkedDeque<Order> accountContainer = symbolOrderContainer.get(order.getVal());
        return accountContainer.remove(order);
    }

    public void insertBid(Order order) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> symbolOrderContainer = bidContainer.get(order.getSymbol());
        insertPrice(symbolOrderContainer, order);
    }

    private void insertPrice(ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> symbolOrderContainer, Order order) {
        ConcurrentLinkedDeque<Order> accountContainer  = new ConcurrentLinkedDeque();
        ConcurrentLinkedDeque<Order> existingAccountContainer = symbolOrderContainer.putIfAbsent(order.getVal(), accountContainer);
        if(existingAccountContainer == null){
            accountContainer.offer(order);
        } else {
            existingAccountContainer.offer(order);
        }
    }
}
