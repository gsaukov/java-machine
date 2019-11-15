package com.apps.potok.exchange.core;

import com.apps.potok.exchange.init.Initiator;
import com.apps.potok.exchange.mkdata.Route;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

@Service
// Sell orders
public class BidContainer {

    @Value("${exchange.order-size}")
    private Integer orderSize;

    private final Initiator initiator;
    private final AtomicLong bidInserted = new AtomicLong(0l);

    private final ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>>> bidContainer = new ConcurrentHashMap<>();

    public BidContainer(Initiator initiator) {
        this.initiator = initiator;
    }

    @PostConstruct
    private void postConstruct (){
        initiator.initiateContainer(orderSize, bidContainer, Route.SELL);
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
