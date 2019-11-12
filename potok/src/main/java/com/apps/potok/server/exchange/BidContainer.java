package com.apps.potok.server.exchange;

import com.apps.potok.server.init.Initiator;
import com.apps.potok.server.mkdata.Route;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

@Service
// Sell orders
public class BidContainer {

    @Value("${exchange.order-size}")
    private Integer orderSize;

    private final Initiator initiator;
    private final AtomicLong bidInserted = new AtomicLong(0l);

    private final ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>>> bidContainer = new ConcurrentHashMap<>();

    public BidContainer(Initiator initiator) {
        this.initiator = initiator;
    }

    @PostConstruct
    private void postConstruct (){
        initiator.initiateContainer(orderSize, bidContainer, Route.SELL);
    }

    public ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>>> get() {
        return bidContainer;
    }

    public ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>> get(String symbol) {
        return bidContainer.get(symbol);
    }

    public boolean containsKey(String symbolName) {
        return bidContainer.containsKey(symbolName);
    }

    public void put(String symbolName, SortedMap<Integer, ConcurrentLinkedQueue<String>> toLeave) {
        bidContainer.put(symbolName, new ConcurrentSkipListMap<>(toLeave));
    }

    public void insertBid(String symbolName, Integer val, String account) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>> symbolOrderContainer = bidContainer.get(symbolName);
        insertPrice(symbolOrderContainer, val, account);
    }

    private void insertPrice(ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>> symbolOrderContainer, Integer val, String account) {
        ConcurrentLinkedQueue<String> accountContainer  = new ConcurrentLinkedQueue();
        ConcurrentLinkedQueue<String> existingAccountContainer = symbolOrderContainer.putIfAbsent(val, accountContainer);
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
        for(Map.Entry<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>>> entry : bidContainer.entrySet()){
            ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>> map = entry.getValue();
            for(ConcurrentLinkedQueue<String> list : map.values()){
                res.getAndAdd(list.size());
            }
        }
        return res.get();
    }

    public long getBidInserted() {
        return bidInserted.get();
    }

}
