package com.apps.potok.server.exchange;

import com.apps.potok.server.init.Inititiator;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SymbolContainer {

    private final ConcurrentHashMap<String, Integer> symbolsContainer;

    public SymbolContainer() {
        this.symbolsContainer = new ConcurrentHashMap<>();
        for(int i = 0 ; i < 1000 ; i++){
            symbolsContainer.put(RandomStringUtils.randomAlphabetic(4).toUpperCase(), RandomUtils.nextInt(10, 90));
        }
    }

    public List<String> getSymbols() {
        return new ArrayList<>(symbolsContainer.keySet());
    }

    public Integer getQuote(String symbol) {
        return symbolsContainer.get(symbol);
    }
}
