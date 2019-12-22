package com.apps.potok.exchange.core;

import com.apps.potok.exchange.eventhandlers.ExecutionNotifierServer;
import com.apps.potok.exchange.eventhandlers.QuoteNotifierServer;
import com.apps.potok.soketio.model.execution.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.apps.potok.exchange.core.Route.BUY;

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

                        notifyPartFilledFilled(matchingOrder, order, matchingOrder.getVal(), order.getVolume());

                        tier.offerFirst(matchingOrder);
                        order.fullFill();

                        return;
                    } else if (matchingOrder.getVolume().compareTo(order.getVolume()) < 0) {
                        //order is part filled produce partfill execution notification continue the loop
                        //matching order filled produce fill notification
                        bid.getAndAdd(matchingOrder.getVolume());
                        order.partFill(matchingOrder);

                        notifyPartFilledFilled(order, matchingOrder, matchingOrder.getVal(), matchingOrder.getVolume());

                        matchingOrder.fullFill();

                    } else {
                        bid.getAndAdd(order.getVolume());
                        //both are filled produce execution notifications for both

                        notifyFilledFilled(matchingOrder, order, matchingOrder.getVal(), matchingOrder.getVolume());

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

                        notifyPartFilledFilled(matchingOrder, order, matchingOrder.getVal(), order.getVolume());

                        tier.offerFirst(matchingOrder);
                        order.fullFill();
                        return;
                    } else if (matchingOrder.getVolume().compareTo(order.getVolume()) < 0) {
                        //order is part filled produce partfill execution notification continue the loop
                        //matching order filled produce fill notification
                        ask.getAndAdd(matchingOrder.getVolume());
                        order.partFill(matchingOrder);

                        notifyPartFilledFilled(order, matchingOrder, matchingOrder.getVal(), matchingOrder.getVolume());

                        matchingOrder.fullFill();
                    } else {
                        //both are filled produce execution notifications for both
                        ask.getAndAdd(order.getVolume());

                        notifyFilledFilled(matchingOrder, order, matchingOrder.getVal(), matchingOrder.getVolume());

                        matchingOrder.fullFill();
                        order.fullFill();
                        return;
                    }
                }
            }
        }
        bidContainer.insertBid(order);
    }

    private void notifyPartFilledFilled(Order partFilledOrder, Order filledOrder, Integer fillPrice, Integer quantity){
        UUID partFilledExecutionUuid = UUID.randomUUID();
        UUID filledExecutionUuid = UUID.randomUUID();

        Execution partFilledExecution = new Execution(partFilledExecutionUuid, filledExecutionUuid, partFilledOrder, fillPrice, quantity, false);
        Execution filledExecution  = new Execution(filledExecutionUuid, partFilledExecutionUuid, filledOrder, fillPrice, quantity, true);
        executionNotifierServer.pushExecution(partFilledExecution);
        executionNotifierServer.pushExecution(filledExecution);
    }

    private void notifyFilledFilled(Order filledOrder1, Order filledOrder2, Integer fillPrice, Integer quantity){
        UUID filledExecution1Uuid = UUID.randomUUID();
        UUID filledExecution2Uuid = UUID.randomUUID();

        Execution filledExecution1 = new Execution(filledExecution1Uuid, filledExecution2Uuid, filledOrder1, fillPrice, quantity, true);
        Execution filledExecution2  = new Execution(filledExecution2Uuid, filledExecution1Uuid, filledOrder2, fillPrice, quantity, true);
        executionNotifierServer.pushExecution(filledExecution1);
        executionNotifierServer.pushExecution(filledExecution2);
    }

    public long getAskExecutions() {
        return ask.get();
    }

    public long getBidExecutions() {
        return bid.get();
    }
}