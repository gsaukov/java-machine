package com.apps.potok.exchange.core;

import com.apps.potok.exchange.eventhandlers.ExecutionNotifierServer;
import com.apps.potok.exchange.eventhandlers.QuoteNotifierServer;
import com.apps.potok.exchange.mkdata.MkData;
import com.apps.potok.exchange.mkdata.MkDataServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static com.apps.potok.exchange.mkdata.Route.BUY;

@Service
public class Exchange extends Thread {

    @Value("${exchange.order-size}")
    private Integer orderSize;

    private final AskContainer askContainer;
    private final BidContainer bidContainer;

    private final AtomicLong ask = new AtomicLong(0l);
    private final AtomicLong bid = new AtomicLong(0l);
    private final AtomicBoolean running = new AtomicBoolean(true);

    private final MkDataServer mkDataServer;

    @Autowired
    private QuoteNotifierServer quoteNotifierServer;

    @Autowired
    private ExecutionNotifierServer executionNotifierServer;

    public Exchange(BidContainer bidContainer, AskContainer askContainer, MkDataServer mkDataServer){
        super.setName("EventNotifierServer");
        this.askContainer = askContainer;
        this.bidContainer = bidContainer;
        this.mkDataServer = mkDataServer;
    }

    @Override
    public void run() {
        while(running.get()){
            pullMkData(orderSize);
        }
     }

    public void stopExchange (){
        running.getAndSet(false);
    }

    private void pullMkData(int size) {
        List<MkData> events = mkDataServer.getMkData(size);
        for(MkData event : events) {
            fireEvent(event);
        }
    }

    public void fireEvent(MkData order) {
        fireOrder(toOrder(order));
    }

    public void fireOrder(Order order) {
        if (BUY.equals(order.getRoute())) {
            fireBuy(order);
        } else {
            fireSell(order);
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

                        return;
                    } else if (matchingOrder.getVolume().compareTo(order.getVolume()) < 0) {
                        //order is part filled produce partfill execution notification continue the loop
                        //matching order filled produce fill notification
                        bid.getAndAdd(matchingOrder.getVolume());
                        order.partFill(matchingOrder);

                        notifyFilled(matchingOrder, matchingOrder.getVal());
                        notifyPartFilled(order, matchingOrder.getVal(), matchingOrder.getVolume());

                    } else {
                        bid.getAndAdd(order.getVolume());
                        //both are filled produce execution notifications for both

                        notifyFilled(matchingOrder, matchingOrder.getVal());
                        notifyFilled(order, matchingOrder.getVal());

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

                        return;
                    } else if (matchingOrder.getVolume().compareTo(order.getVolume()) < 0) {
                        //order is part filled produce partfill execution notification continue the loop
                        //matching order filled produce fill notification
                        ask.getAndAdd(matchingOrder.getVolume());
                        order.partFill(matchingOrder);

                        notifyFilled(matchingOrder, matchingOrder.getVal());
                        notifyPartFilled(order, matchingOrder.getVal(), matchingOrder.getVolume());

                    } else {
                        //both are filled produce execution notifications for both
                        ask.getAndAdd(order.getVolume());

                        notifyFilled(matchingOrder, matchingOrder.getVal());
                        notifyFilled(order, matchingOrder.getVal());

                        return;
                    }
                }
            }
        }
        bidContainer.insertBid(order);
    }

    private Order toOrder(MkData event){
        return new Order(event.getSymbol(), event.getAccount(), event.getRoute(), event.getVal(), event.getVolume());
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