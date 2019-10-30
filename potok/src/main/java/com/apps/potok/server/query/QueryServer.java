package com.apps.potok.server.query;

import com.apps.potok.server.exchange.BidContainer;
import com.apps.potok.soketio.model.quote.Quote;
import com.apps.potok.soketio.model.quote.QuoteResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class QueryServer {

    private static QuoteResponse EMPTY_RESPONSE = new QuoteResponse(new ArrayList<>());

    private BidContainer alertContainer;

    public QueryServer(BidContainer alertContainer) {
        this.alertContainer = alertContainer;
    }

    public void printSymbols (){
        for(Map.Entry<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> entry : alertContainer.get().entrySet()){
            String symbolName = entry.getKey();
            ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> prices = entry.getValue();
            Integer minPrice = prices.firstEntry().getKey();
            Integer maxPrice =  prices.lastEntry().getKey();
            System.out.println(" Symbol : " + symbolName + " price range is from " + minPrice + " to " + maxPrice);
        }
    }

    public QuoteResponse searchOffers(String symbolName, Integer maxDesirablePrice){
        if(!alertContainer.containsKey(symbolName)){
            return EMPTY_RESPONSE;
        }
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> offers = alertContainer.get(symbolName);
        return prepareQuoteResponse(symbolName, offers.tailMap(maxDesirablePrice));
    }

    public QuoteResponse searchAllOffers(String symbolName){
        if(!alertContainer.containsKey(symbolName)){
            return EMPTY_RESPONSE;
        }
        return prepareQuoteResponse(symbolName, alertContainer.get(symbolName));
    }

    private QuoteResponse prepareQuoteResponse(String symbolName, Map<Integer, CopyOnWriteArrayList<String>> offers){
        List<Quote> quotes = new ArrayList<>();

        for(Map.Entry<Integer, CopyOnWriteArrayList<String>> entry : offers.entrySet()){
            Integer value = entry.getKey();
//            String userNames = "";
//            for(String name : entry.getValue()){
//                userNames += (" [" +  name + "],");
//            }
            quotes.add(new Quote(symbolName, value));
        }
        return new QuoteResponse(quotes);
    }

}
