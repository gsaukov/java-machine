package com.apps.potok.exchange.query;

import com.apps.potok.exchange.core.AskContainer;
import com.apps.potok.exchange.core.BidContainer;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.Route;
import com.apps.potok.soketio.model.quote.Quote;
import com.apps.potok.soketio.model.quote.QuoteResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class QuoteManager {

    private static QuoteResponse EMPTY_RESPONSE = new QuoteResponse(new ArrayList<>(), new ArrayList<>());

    private AskContainer askContainer;
    private BidContainer bidContainer;

    public QuoteManager(BidContainer bidContainer, AskContainer askContainer) {
        this.bidContainer = bidContainer;
        this.askContainer = askContainer;
    }

    public void printSymbols (){
        for(Map.Entry<String, ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>>> entry : bidContainer.get().entrySet()){
            String symbolName = entry.getKey();
            ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> prices = entry.getValue();
            Integer minPrice = prices.firstEntry().getKey();
            Integer maxPrice =  prices.lastEntry().getKey();
            System.out.println(" Symbol : " + symbolName + " price range is from " + minPrice + " to " + maxPrice);
        }
    }

    public QuoteResponse searchBids(String symbolName, Integer maxDesirablePrice){
        if(!bidContainer.containsKey(symbolName)){
            return EMPTY_RESPONSE;
        }
        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> offers = bidContainer.get(symbolName);
        List<Quote> bidQuotes = prepareQuoteResponse(symbolName, offers.tailMap(maxDesirablePrice), Route.SELL);
        return new QuoteResponse(bidQuotes, null);
    }

    public QuoteResponse searchAllOffers(String symbolName){
        List<Quote> bidQuotes = prepareQuoteResponse(symbolName, bidContainer.get(symbolName), Route.SELL);
        List<Quote> askQuotes = prepareQuoteResponse(symbolName, askContainer.get(symbolName), Route.BUY);
        return new QuoteResponse(bidQuotes, askQuotes);
    }

    private List<Quote> prepareQuoteResponse(String symbolName, Map<Integer, ConcurrentLinkedDeque<Order>> offers, Route route){
        List<Quote> quotes = new ArrayList<>();

        if(offers!=null){
            for(Map.Entry<Integer, ConcurrentLinkedDeque<Order>> entry : offers.entrySet()){
                if(entry.getValue() != null && !entry.getValue().isEmpty()) {
                    Integer value = entry.getKey();
                    Integer tierVolume = getTierVolume(entry.getValue());
                    quotes.add(new Quote(symbolName, value, tierVolume, route));
                }
            }
        }
        return quotes;
    }

    private Integer getTierVolume(ConcurrentLinkedDeque<Order> priceTier){
        Integer tierVolume = new Integer(0);
        for(Order order : priceTier){
            tierVolume += order.getVolume();
        }
        return tierVolume;
    }

}
