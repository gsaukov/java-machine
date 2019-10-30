package com.apps.potok.server.exchange;

import com.apps.potok.server.init.Inititiator;
import com.apps.potok.server.mkdata.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
//Buy orders
public class AskContainer {

    private SymbolContainer symbolContainer;
    private Inititiator inititiator;

    private final HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> askContainer = new HashMap<>();

    public AskContainer(SymbolContainer symbolContainer, Inititiator inititiator) {
        this.inititiator = inititiator;
        this.symbolContainer = symbolContainer;
        inititiator.initiateContainer(10000, askContainer, Route.BUY);
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

}
