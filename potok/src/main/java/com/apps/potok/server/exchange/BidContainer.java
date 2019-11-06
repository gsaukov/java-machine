package com.apps.potok.server.exchange;

import com.apps.potok.server.init.Initiator;
import com.apps.potok.server.mkdata.Route;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
// Sell orders
public class BidContainer {

    private final SymbolContainer symbolContainer;
    private final Initiator initiator;

    private final HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> bidContainer = new HashMap<>();

    public BidContainer(SymbolContainer symbolContainer, Initiator initiator) {
        this.initiator = initiator;
        this.symbolContainer = symbolContainer;
        initiator.initiateContainer(10000, bidContainer, Route.SELL);
    }

    public HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> get() {
        return bidContainer;
    }

    public ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> get(String symbol) {
        return bidContainer.get(symbol);
    }

    public boolean containsKey(String symbolName) {
        return bidContainer.containsKey(symbolName);
    }

    public void put(String symbolName, SortedMap<Integer, CopyOnWriteArrayList<String>> toLeave) {
        bidContainer.put(symbolName, new ConcurrentSkipListMap<>(toLeave));
    }

    public void insertBid(String symbolName, Integer val, String account) {
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> symbolOrderContainer = bidContainer.get(symbolName);
        insertPrice(symbolOrderContainer, val, account);
    }

    private void insertPrice(ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> symbolOrderContainer, Integer val, String account) {
        CopyOnWriteArrayList<String> customerContainer = symbolOrderContainer.get(val);
        if(customerContainer == null){
            customerContainer = new CopyOnWriteArrayList();
            symbolOrderContainer.put(val, customerContainer);
        }
        customerContainer.add(account);
    }
}
