package com.apps.potok.server.query;

import com.apps.potok.server.exchange.AskContainer;
import com.apps.potok.server.exchange.BidContainer;
import com.apps.potok.server.mkdata.Route;
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

    private static QuoteResponse EMPTY_RESPONSE = new QuoteResponse(new ArrayList<>(), new ArrayList<>());

    private AskContainer askContainer;
    private BidContainer bidContainer;

    public QueryServer(BidContainer bidContainer, AskContainer askContainer) {
        this.bidContainer = bidContainer;
        this.askContainer = askContainer;
    }

    public void printSymbols (){
        for(Map.Entry<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> entry : bidContainer.get().entrySet()){
            String symbolName = entry.getKey();
            ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> prices = entry.getValue();
            Integer minPrice = prices.firstEntry().getKey();
            Integer maxPrice =  prices.lastEntry().getKey();
            System.out.println(" Symbol : " + symbolName + " price range is from " + minPrice + " to " + maxPrice);
        }
    }

    public QuoteResponse searchBids(String symbolName, Integer maxDesirablePrice){
        if(!bidContainer.containsKey(symbolName)){
            return EMPTY_RESPONSE;
        }
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> offers = bidContainer.get(symbolName);
        List<Quote> bidQuotes = prepareQuoteResponse(symbolName, offers.tailMap(maxDesirablePrice), Route.SELL);
        return new QuoteResponse(bidQuotes, null);
    }

    public QuoteResponse searchAllOffers(String symbolName){
        List<Quote> bidQuotes = prepareQuoteResponse(symbolName, bidContainer.get(symbolName), Route.SELL);
        List<Quote> askQuotes = prepareQuoteResponse(symbolName, askContainer.get(symbolName), Route.BUY);
        return new QuoteResponse(bidQuotes, askQuotes);
    }

    private List<Quote> prepareQuoteResponse(String symbolName, Map<Integer, CopyOnWriteArrayList<String>> offers, Route route){
        List<Quote> quotes = new ArrayList<>();

        if(offers!=null){
            for(Map.Entry<Integer, CopyOnWriteArrayList<String>> entry : offers.entrySet()){
                Integer value = entry.getKey();
    //            String userNames = "";
    //            for(String name : entry.getValue()){
    //                userNames += (" [" +  name + "],");
    //            }
                quotes.add(new Quote(symbolName, value, route));
            }
        }
        return quotes;
    }

}