package com.apps.potok.exchange.core;

import com.apps.potok.exchange.eventhandlers.ExecutionNotifierServer;
import com.apps.potok.exchange.eventhandlers.QuoteNotifierServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.apps.potok.exchange.mkdata.Route.BUY;

@Service
public class Exchange {

    private final AskContainer askContainer;
    private final BidContainer bidContainer;

    private final AtomicLong ask = new AtomicLong(0l);
    private final AtomicLong bid = new AtomicLong(0l);

    @Autowired
    private QuoteNotifierServer quoteNotifierServer;

    @Autowired
    private ExecutionNotifierServer executionNotifierServer;

    @Autowired
    private OrderManager orderManager;

    public Exchange(BidContainer bidContainer, AskContainer askContainer){
        this.askContainer = askContainer;
        this.bidContainer = bidContainer;
    }

    public void fireOrder(Order order) {
        orderManager.addOrder(order);
        if (BUY.equals(order.getRoute())) {
            fireBuy(order);
        } else {
            fireSell(order); //or short
        }
        quoteNotifierServer.pushQuote(order.getSymbol());
    }

    //TODO i think after part fill incoming orders should refired ot fireBuy/fireSell since market could have changed.

    private void fireBuy(Order order) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> map = bidContainer.get(order.getSymbol());
        ConcurrentNavigableMap<Integer, ConcurrentLinkedDeque<Order>> toFire = map.headMap(order.getVal(), true);
        for(Map.Entry<Integer, ConcurrentLinkedDeque<Order>> fired : toFire.entrySet()){
            ConcurrentLinkedDeque<Order> tier = fired.getValue();
            if(!tier.isEmpty()) {
                Order matchingOrder;
                while ((matchingOrder = tier.poll()) != null && matchingOrder.isActive()) {
                    if(matchingOrder.getVolume().compareTo(order.getVolume()) > 0){
                        //order is filled produce execution notification
                        //matching order partfilled produce part fill notification and return it to rhe head of the queue
                        bid.getAndAdd(order.getVolume());
                        matchingOrder.partFill(order);
                        tier.offerFirst(matchingOrder);

                        notifyFilled(order, matchingOrder.getVal());
                        notifyPartFilled(matchingOrder, matchingOrder.getVal(), order.getVolume());

                        order.fullFill();

                        return;
                    } else if (matchingOrder.getVolume().compareTo(order.getVolume()) < 0) {
                        //order is part filled produce partfill execution notification continue the loop
                        //matching order filled produce fill notification
                        bid.getAndAdd(matchingOrder.getVolume());
                        order.partFill(matchingOrder);

                        notifyFilled(matchingOrder, matchingOrder.getVal());
                        notifyPartFilled(order, matchingOrder.getVal(), matchingOrder.getVolume());

                        matchingOrder.fullFill();

                    } else {
                        bid.getAndAdd(order.getVolume());
                        //both are filled produce execution notifications for both

                        notifyFilled(matchingOrder, matchingOrder.getVal());
                        notifyFilled(order, matchingOrder.getVal());

                        matchingOrder.fullFill();
                        order.fullFill();
                        return;
                    }
                }
            }
        }
        askContainer.insertAsk(order);
    }

    private void fireSell(Order order) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> map = askContainer.get(order.getSymbol());
        ConcurrentNavigableMap<Integer, ConcurrentLinkedDeque<Order>> toFire = map.headMap(order.getVal(), true);
        for(Map.Entry<Integer, ConcurrentLinkedDeque<Order>> fired : toFire.entrySet()){
            ConcurrentLinkedDeque<Order> tier = fired.getValue();
            if(!tier.isEmpty()) {
                Order matchingOrder;
                while ((matchingOrder = tier.poll()) != null && matchingOrder.isActive()) {
                    if(matchingOrder.getVolume().compareTo(order.getVolume()) > 0){
                        //order is filled produce execution notification
                        //matching order partfilled produce part fill notification and return to rhe head of the queue
                        ask.getAndAdd(order.getVolume());
                        matchingOrder.partFill(order);
                        tier.offerFirst(matchingOrder);

                        notifyFilled(order, matchingOrder.getVal());
                        notifyPartFilled(matchingOrder, matchingOrder.getVal(), order.getVolume());

                        order.fullFill();
                        return;
                    } else if (matchingOrder.getVolume().compareTo(order.getVolume()) < 0) {
                        //order is part filled produce partfill execution notification continue the loop
                        //matching order filled produce fill notification
                        ask.getAndAdd(matchingOrder.getVolume());
                        order.partFill(matchingOrder);

                        notifyFilled(matchingOrder, matchingOrder.getVal());
                        notifyPartFilled(order, matchingOrder.getVal(), matchingOrder.getVolume());
                        matchingOrder.fullFill();
                    } else {
                        //both are filled produce execution notifications for both
                        ask.getAndAdd(order.getVolume());

                        notifyFilled(matchingOrder, matchingOrder.getVal());
                        notifyFilled(order, matchingOrder.getVal());

                        matchingOrder.fullFill();
                        order.fullFill();
                        return;
                    }
                }
            }
        }
        bidContainer.insertBid(order);
    }

    private void notifyPartFilled(Order order, Integer fillPrice, Integer quantity){
        executionNotifierServer.pushPartFill(order, fillPrice, quantity);
    }

    private void notifyFilled(Order order, Integer fillPrice){
        executionNotifierServer.pushFill(order, fillPrice);
    }

    public long getAskExecutions() {
        return ask.get();
    }

    public long getBidExecutions() {
        return bid.get();
    }
}