package com.apps.potok.server.exchange;

import com.apps.potok.server.init.Initiator;
import com.apps.potok.server.mkdata.Route;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
//Buy orders
public class AskContainer {

    private final SymbolContainer symbolContainer;
    private final Initiator initiator;

    private final HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> askContainer = new HashMap<>();

    public AskContainer(SymbolContainer symbolContainer, Initiator initiator) {
        this.initiator = initiator;
        this.symbolContainer = symbolContainer;
        initiator.initiateContainer(10000, askContainer, Route.BUY);
    }

    public HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> get() {
        return askContainer;
    }

    public ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> get(String symbol) {
        return askContainer.get(symbol);
    }

    public boolean containsKey(String symbolName) {
        return askContainer.containsKey(symbolName);
    }

    public void put(String symbolName, SortedMap<Integer, CopyOnWriteArrayList<String>> toLeave) {
        askContainer.put(symbolName, new ConcurrentSkipListMap<>(toLeave));
    }

    public void insertAsk(String symbolName, Integer val, String account) {
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> symbolOrderContainer = askContainer.get(symbolName);
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
