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
    private final AtomicLong bidInserted = new AtomicLong(0l);

    private final ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>>> bidContainer;

    public BidContainer() {
        this.bidContainer = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>>> get() {
        return bidContainer;
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
            bidInserted.getAndAdd(order.getVolume());
        } else {
            existingAccountContainer.offer(order);
            bidInserted.getAndAdd(order.getVolume());
        }
    }

    public Long size(){
        AtomicLong res = new AtomicLong(0l);
        for(Map.Entry<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>>> entry : bidContainer.entrySet()){
            ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> map = entry.getValue();
            for(ConcurrentLinkedDeque<Order> list : map.values()){
                for(Order order : list){
                    res.getAndAdd(order.getVolume());
                }
            }
        }
        return res.get();
    }

    public long getBidInserted() {
        return bidInserted.get();
    }

}
