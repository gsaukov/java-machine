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
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.apps.potok.server.mkdata.Route.BUY;

@Service
public class Exchange extends Thread {

    private final AskContainer askContainer;
    private final BidContainer bidContainer;
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
        while(true){
            fireAll(size * RandomUtils.nextInt(1, 3));
//            System.out.println("done");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void fireAll(int size) {
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
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> map = bidContainer.get(event.getSymbol());

        SortedMap<Integer, CopyOnWriteArrayList<String>> toFire = map.headMap(event.getVal(), true);
        if (toFire.isEmpty()){
            askContainer.insertAsk(event.getSymbol(), event.getVal(), event.getAccount());
            return;
        }

        for(Map.Entry<Integer, CopyOnWriteArrayList<String>> fired : toFire.entrySet()){
            for (String customer : fired.getValue()){
//                System.out.println(System.currentTimeMillis() + " FIRE: " + event.getSymbol() + " at " + event.getVal() + " for " + customer);
            }
        }

        SortedMap<Integer, CopyOnWriteArrayList<String>> toLeave = map.tailMap(event.getVal());
        bidContainer.put(event.getSymbol(), toLeave);
    }

    private void fireSell(MkData event) {
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> map = askContainer.get(event.getSymbol());

        SortedMap<Integer, CopyOnWriteArrayList<String>> toFire = map.tailMap(event.getVal(), true);
        if (toFire.isEmpty()){
            bidContainer.insertBid(event.getSymbol(), event.getVal(), event.getAccount());
            return;
//            System.out.println(System.currentTimeMillis() + " nothing to FIRE for: " + event.getSymbol() + " at " + event.getVal());
        }

        for(Map.Entry<Integer, CopyOnWriteArrayList<String>> fired : toFire.entrySet()){
            for (String customer : fired.getValue()){
//                System.out.println(System.currentTimeMillis() + " FIRE: " + event.getSymbol() + " at " + event.getVal() + " for " + customer);
            }
        }

        SortedMap<Integer, CopyOnWriteArrayList<String>> toLeave = map.headMap(event.getVal());
        askContainer.put(event.getSymbol(), toLeave);
    }

    private MkData toMkData(Order order){
        return new MkData(order.getSymbol(), order.getVal(), order.getRoute(), order.getAccount());
    }

}