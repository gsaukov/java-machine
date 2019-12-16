package com.apps.potok.exchange.core;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SymbolContainer {

    private final ConcurrentHashMap<String, Integer> symbolsContainer;
    private final List<String> symbols = new ArrayList<>();

    public SymbolContainer() {
        this.symbolsContainer = new ConcurrentHashMap<>();
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void addSymbol(String symbol, Integer val) {
        symbolsContainer.put(symbol, val);
        symbols.add(symbol);
    }

    public String get(int i) {
        return symbols.get(i);
    }

    public Integer getQuote(String symbol) {
        return symbolsContainer.get(symbol);
    }
}
