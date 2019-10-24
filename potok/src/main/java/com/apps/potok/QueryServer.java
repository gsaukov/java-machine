package com.apps.potok;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class QueryServer {

    AlertContainer alertContainer;

    public void printSymbols (){
        for(Map.Entry<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> entry : alertContainer.get().entrySet()){
            String symbolName = entry.getKey();
            ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> prices = entry.getValue();
            Integer minPrice = prices.firstEntry().getKey();
            Integer maxPrice =  prices.lastEntry().getKey();
            System.out.println(" Symbol : " + symbolName + " price range is from " + minPrice + " to " + maxPrice);
        }
    }

    public void searchOffers(String symbolName, Integer maxDesirablePrice){
        if(!alertContainer.containsKey(symbolName)){
            System.out.println("Nothing found for symbol" + symbolName);
        }
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> offers = alertContainer.get(symbolName);
        printOffers(symbolName, offers.tailMap(maxDesirablePrice));
    }

    private void printOffers(String symbolName, Map<Integer, CopyOnWriteArrayList<String>> offers){
        for(Map.Entry<Integer, CopyOnWriteArrayList<String>> entry : offers.entrySet()){
            Integer value = entry.getKey();
            String userNames = "";
            for(String name : entry.getValue()){
                userNames += (" [" +  name + "],");
            }
            System.out.println(" Following offers for item: " + symbolName + " " + value + " users " + userNames);
        }
    }

}
