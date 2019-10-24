package com.apps.potok;

import com.apps.potok.init.Inititiator;

import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AlertContainer {

    private HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> alertContainer = new HashMap<>();
    private List<String> symbols;

    public AlertContainer() {
        this.symbols = Inititiator.getSymbols(10000);
        Inititiator.initiateContainer(10000, alertContainer, symbols);
    }


    public HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> get() {
        return alertContainer;
    }

    public ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> get(String symbol) {
        return alertContainer.get(symbol);
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public boolean containsKey(String symbolName) {
        return alertContainer.containsKey(symbolName);
    }

    public void put(String symbolName, SortedMap<Integer, CopyOnWriteArrayList<String>> toLeave) {
        alertContainer.put(symbolName, new ConcurrentSkipListMap<>(toLeave));
    }

}
