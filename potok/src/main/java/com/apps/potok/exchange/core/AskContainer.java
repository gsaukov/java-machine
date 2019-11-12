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
//Buy orders
public class AskContainer {

    @Value("${exchange.order-size}")
    private Integer orderSize;

    private final Initiator initiator;
    public final AtomicLong askInserted = new AtomicLong(0l);

    private final ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>>> askContainer = new ConcurrentHashMap<>();

    public AskContainer(Initiator initiator) {
        this.initiator = initiator;
    }

    @PostConstruct
    private void postConstruct (){
        initiator.initiateContainer(orderSize, askContainer, Route.BUY);
    }


    public ConcurrentHashMap<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>>> get() {
        return askContainer;
    }

    public ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>> get(String symbol) {
        return askContainer.get(symbol);
    }

    public boolean containsKey(String symbolName) {
        return askContainer.containsKey(symbolName);
    }

    public void put(String symbolName, SortedMap<Integer, ConcurrentLinkedDeque<String>> toLeave) {
        askContainer.put(symbolName, new ConcurrentSkipListMap<>(toLeave));
    }

    public void insertAsk(String symbolName, Integer val, String account) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>> symbolOrderContainer = askContainer.get(symbolName);
        insertPrice(symbolOrderContainer, val, account);
    }

    private void insertPrice(ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>> symbolOrderContainer, Integer val, String account) {
        final ConcurrentLinkedDeque<String> accountContainer  = new ConcurrentLinkedDeque();
        final ConcurrentLinkedDeque<String> existingAccountContainer = symbolOrderContainer.putIfAbsent(val, accountContainer);
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
        for(Map.Entry<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>>> entry : askContainer.entrySet()){
            ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<String>> map = entry.getValue();
            for(ConcurrentLinkedDeque<String> list : map.values()){
                res.getAndAdd(list.size());
            }
        }
        return res.get();
    }

    public long getAskInserted() {
        return askInserted.get();
    }
}
