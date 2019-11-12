package com.apps.potok.exchange.core;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SymbolContainer {

    @Value("${exchange.symbol-size}")
    private Integer symbolSize;

    @Value("${exchange.price.min-range}")
    private Integer minRange;

    @Value("${exchange.price.max-range}")
    private Integer maxRange;

    private final ConcurrentHashMap<String, Integer> symbolsContainer;

    public SymbolContainer() {
        this.symbolsContainer = new ConcurrentHashMap<>();
    }

    @PostConstruct
    private void postConstruct (){
        for(int i = 0 ; i < symbolSize ; i++){
            symbolsContainer.put(RandomStringUtils.randomAlphabetic(4).toUpperCase(), RandomUtils.nextInt(minRange, maxRange));
        }
    }

    public List<String> getSymbols() {
        return new ArrayList<>(symbolsContainer.keySet());
    }

    public Integer getQuote(String symbol) {
        return symbolsContainer.get(symbol);
    }
}
