package com.apps.potok.exchange.core;

import com.apps.potok.exchange.eventhandlers.EventNotifierServerV2;
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
    private EventNotifierServerV2 eventNotifierServer;

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
        eventNotifierServer.pushEvent(order.getSymbol());
    }

    private void fireBuy(Order order) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> map = bidContainer.get(order.getSymbol());
        ConcurrentNavigableMap<Integer, ConcurrentLinkedDeque<Order>> toFire = map.headMap(order.getVal(), true);
        for(Map.Entry<Integer, ConcurrentLinkedDeque<Order>> fired : toFire.entrySet()){
            ConcurrentLinkedDeque<Order> tier = fired.getValue();
            if(!tier.isEmpty()) {
                Order matchingOrder;
                while ((matchingOrder = tier.poll()) != null) {
                    bid.getAndIncrement();
                    return;
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
                while ((matchingOrder = tier.poll()) != null) {
                    ask.getAndIncrement();
                    return;
                }
            }
        }
        bidContainer.insertBid(order);
    }

    private Order toOrder(MkData event){
        return new Order(event.getSymbol(), event.getAccount(), event.getRoute(), event.getVal(), event.getVolume());
    }

    public long getAskExecutions() {
        return ask.get();
    }

    public long getBidExecutions() {
        return bid.get();
    }
}