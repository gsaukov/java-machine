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

    private final ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>>> bidContainer = new ConcurrentHashMap<>();

    public BidContainer(Initiator initiator) {
        this.initiator = initiator;
    }

    @PostConstruct
    private void postConstruct (){
        initiator.initiateContainer(orderSize, bidContainer, Route.SELL);
    }

    public ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>>> get() {
        return bidContainer;
    }

    public ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>> get(String symbol) {
        return bidContainer.get(symbol);
    }

    public boolean containsKey(String symbolName) {
        return bidContainer.containsKey(symbolName);
    }

    public void put(String symbolName, SortedMap<Integer, ConcurrentLinkedDeque<String>> toLeave) {
        bidContainer.put(symbolName, new ConcurrentSkipListMap<>(toLeave));
    }

    public void insertBid(String symbolName, Integer val, String account) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>> symbolOrderContainer = bidContainer.get(symbolName);
        insertPrice(symbolOrderContainer, val, account);
    }

    private void insertPrice(ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>> symbolOrderContainer, Integer val, String account) {
        ConcurrentLinkedDeque<String> accountContainer  = new ConcurrentLinkedDeque();
        ConcurrentLinkedDeque<String> existingAccountContainer = symbolOrderContainer.putIfAbsent(val, accountContainer);
        if(existingAccountContainer == null){
            accountContainer.offer(account);
            bidInserted.incrementAndGet();
        } else {
            existingAccountContainer.offer(account);
            bidInserted.incrementAndGet();
        }
    }

    public Long size(){
        AtomicLong res = new AtomicLong(0l);
        for(Map.Entry<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>>> entry : bidContainer.entrySet()){
            ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>> map = entry.getValue();
            for(ConcurrentLinkedDeque<String> list : map.values()){
                res.getAndAdd(list.size());
            }
        }
        return res.get();
    }

    public long getBidInserted() {
        return bidInserted.get();
    }

}
