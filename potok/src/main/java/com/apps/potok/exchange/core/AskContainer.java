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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

@Service
//Buy orders
public class AskContainer {

    @Value("${exchange.order-size}")
    private Integer orderSize;

    private final Initiator initiator;
    public final AtomicLong askInserted = new AtomicLong(0l);

    private final ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>>> askContainer = new ConcurrentHashMap<>();

    public AskContainer(Initiator initiator) {
        this.initiator = initiator;
    }

    @PostConstruct
    private void postConstruct (){
        initiator.initiateContainer(orderSize, askContainer, Route.BUY);
    }


    public ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>>> get() {
        return askContainer;
    }

    public ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>> get(String symbol) {
        return askContainer.get(symbol);
    }

    public boolean containsKey(String symbolName) {
        return askContainer.containsKey(symbolName);
    }

    public void put(String symbolName, SortedMap<Integer, ConcurrentLinkedQueue<String>> toLeave) {
        askContainer.put(symbolName, new ConcurrentSkipListMap<>(toLeave));
    }

    public void insertAsk(String symbolName, Integer val, String account) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>> symbolOrderContainer = askContainer.get(symbolName);
        insertPrice(symbolOrderContainer, val, account);
    }

    private void insertPrice(ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>> symbolOrderContainer, Integer val, String account) {
        final ConcurrentLinkedQueue<String> accountContainer  = new ConcurrentLinkedQueue();
        final ConcurrentLinkedQueue<String> existingAccountContainer = symbolOrderContainer.putIfAbsent(val, accountContainer);
        if(existingAccountContainer == null){
            accountContainer.offer(account);
            askInserted.incrementAndGet();
        } else {
            askInserted.incrementAndGet();
            existingAccountContainer.offer(account);
        }
    }

    public Long size(){
        AtomicLong res = new AtomicLong(0l);
        for(Map.Entry<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>>> entry : askContainer.entrySet()){
            ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>> map = entry.getValue();
            for(ConcurrentLinkedQueue<String> list : map.values()){
                res.getAndAdd(list.size());
            }
        }
        return res.get();
    }

    public long getAskInserted() {
        return askInserted.get();
    }
}
