package com.apps.potok.server.exchange;

import com.apps.potok.server.eventhandlers.EventNotifierServer;
import com.apps.potok.server.eventhandlers.EventNotifierServerV2;
import com.apps.potok.server.mkdata.MkData;
import com.apps.potok.server.mkdata.MkDataServer;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationHandler;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static com.apps.potok.server.mkdata.Route.BUY;

@Service
public class Exchange extends Thread {

    private final AskContainer askContainer;
    private final BidContainer bidContainer;

    private final AtomicLong ask = new AtomicLong(0l);
    private final AtomicLong bid = new AtomicLong(0l);

    private final MkDataServer mkDataServer;
    private int size = 10000;

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
        int i = 0;
        while(i<600){
            pullMkData(size * 2);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }

        System.out.println("done pull MkData");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long askLeft = askContainer.size();
        long askDecrement = ask.get();
        long askInserted = askContainer.askInserted.get();

        long bidLeft = bidContainer.size();
        long bidDecrement = bid.get();
        long bidInserted = bidContainer.bidInserted.get();

        System.out.println("AskInit: 10000 AskLeft: " + askLeft + " AskInserted: " + askInserted + " AskDecrement: " + askDecrement + " check: askInit + askInserted - askDecremen = " + (10000 + askInserted - askDecrement) + " must equal to ask left." );
        System.out.println("BidInit: 10000 BidLeft: " + bidLeft + " BidInserted: " + bidInserted + " BidDecrement: " + bidDecrement + " check: askInit + bidInserted - bidDecremen = " + (10000 + bidInserted - bidDecrement) + " must equal to bid left." );
        System.out.println("Total check: askInserted + askDecremen + bidInserted + bidDecremen = " + (askInserted + askDecrement + bidInserted + bidDecrement) + " must equal to total Order/MkData Issued");
    }

    private void pullMkData(int size) {
        fire(mkDataServer.getMkData(size));
    }

    private void fire(List<MkData> events) {
        for(MkData event : events) {
            fireEvent(event);
        }
    }

    public void fireOrder(Order order) {
        fireEvent(toMkData(order));
    }

    public void fireEvent(MkData event ) {
        if (BUY.equals(event.getRoute())) {
            fireBuy(event);
        } else {
            fireSell(event);
        }
        eventNotifierServer.pushEvent(event.getSymbol());
    }

    private void fireBuy(MkData event) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>> map = bidContainer.get(event.getSymbol());

        ConcurrentNavigableMap<Integer, ConcurrentLinkedQueue<String>> toFire = map.headMap(event.getVal(), true);

        for(Map.Entry<Integer, ConcurrentLinkedQueue<String>> fired : toFire.entrySet()){
            ConcurrentLinkedQueue<String> tier = fired.getValue();
            if(!tier.isEmpty()) {
                String order;
                while ((order = tier.poll()) != null) {
                    bid.getAndIncrement();
                    return;
                }
            }
        }

        askContainer.insertAsk(event.getSymbol(), event.getVal(), event.getAccount());

    }

    private void fireSell(MkData event) {
        ConcurrentSkipListMap<Integer, ConcurrentLinkedQueue<String>> map = askContainer.get(event.getSymbol());

        ConcurrentNavigableMap<Integer, ConcurrentLinkedQueue<String>> toFire = map.headMap(event.getVal(), true);

        for(Map.Entry<Integer, ConcurrentLinkedQueue<String>> fired : toFire.entrySet()){

            ConcurrentLinkedQueue<String> tier = fired.getValue();
            if(!tier.isEmpty()) {
                String order;
                while ((order = tier.poll()) != null) {
                    ask.getAndIncrement();
                    return;
                }
            }
        }

        bidContainer.insertBid(event.getSymbol(), event.getVal(), event.getAccount());
    }

    private MkData toMkData(Order order){
        return new MkData(order.getSymbol(), order.getVal(), order.getRoute(), order.getAccount());
    }

}