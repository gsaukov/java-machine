package com.apps.potok.exchange.core;

import com.apps.potok.exchange.notifiers.ExecutionNotifier;
import com.apps.potok.exchange.notifiers.QuoteNotifier;
import com.apps.potok.soketio.model.execution.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.apps.potok.exchange.core.Route.BUY;

@Service
public class Exchange {

    private final AskContainer askContainer;
    private final BidContainer bidContainer;

    @Autowired
    private QuoteNotifier quoteNotifier;

    @Autowired
    private ExecutionNotifier executionNotifier;

    public Exchange(BidContainer bidContainer, AskContainer askContainer){
        this.askContainer = askContainer;
        this.bidContainer = bidContainer;
    }

    public void fireOrder(Order order) {
        if (BUY.equals(order.getRoute())) {
            fireBuy(order);
        } else {
            fireSell(order); //or short
        }
        quoteNotifier.pushQuote(order.getSymbol());
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
                        orderFilledMatchingPartFilled(order, matchingOrder, tier);
                        return;
                    } else if (matchingOrder.getVolume().compareTo(order.getVolume()) < 0) {
                        orderPartFilledMatchingFilled(order, matchingOrder);
                    } else {
                        orderFilledMatchingFilled(order, matchingOrder);
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
                        orderFilledMatchingPartFilled(order, matchingOrder, tier);
                        return;
                    } else if (matchingOrder.getVolume().compareTo(order.getVolume()) < 0) {
                        orderPartFilledMatchingFilled(order, matchingOrder);
                    } else {
                        orderFilledMatchingFilled(order, matchingOrder);
                        return;
                    }
                }
            }
        }
        bidContainer.insertBid(order);
    }

    private void orderFilledMatchingPartFilled(Order order, Order matchingOrder, ConcurrentLinkedDeque<Order> tier){
        //order is filled produce execution notification
        //matching order partfilled produce part fill notification and return it to rhe head of the queue
        Integer executionVolume = order.getVolume();
        Integer executionPrice = matchingOrder.getVal();

        Integer matchingOrderLeftVolume = matchingOrder.partFill(order);

        executionsPartFilledFilled(matchingOrder, order, executionPrice, executionVolume, matchingOrderLeftVolume);

        tier.offerFirst(matchingOrder);
        order.fullFill();
    }

    private void orderPartFilledMatchingFilled(Order order, Order matchingOrder){
        //order is part filled produce partfill execution notification continue the loop
        //matching order filled produce fill notification
        Integer executionVolume = matchingOrder.getVolume();
        Integer executionPrice = matchingOrder.getVal();

        Integer orderLeftVolume = order.partFill(matchingOrder);

        executionsPartFilledFilled(order, matchingOrder, executionPrice, executionVolume, orderLeftVolume);

        matchingOrder.fullFill();
    }

    private void orderFilledMatchingFilled(Order order, Order matchingOrder){
        //both are filled produce execution notifications for both
        Integer executionVolume = matchingOrder.getVolume();
        Integer executionPrice = matchingOrder.getVal();

        executionsFilledFilled(matchingOrder, order, executionPrice, executionVolume);

        matchingOrder.fullFill();
        order.fullFill();
    }


    private void executionsPartFilledFilled(Order partFilledOrder, Order filledOrder, Integer executionPrice, Integer executionVolume, Integer partFilledLeftQuantity){
        UUID partFilledExecutionUuid = UUID.randomUUID();
        UUID filledExecutionUuid = UUID.randomUUID();

        Execution partFilledExecution = new Execution(partFilledExecutionUuid, filledExecutionUuid, partFilledOrder, executionPrice, executionVolume, partFilledLeftQuantity, false);
        Execution filledExecution  = new Execution(filledExecutionUuid, partFilledExecutionUuid, filledOrder, executionPrice, executionVolume, 0,true);
        executionNotifier.pushExecution(partFilledExecution);
        executionNotifier.pushExecution(filledExecution);
    }

    private void executionsFilledFilled(Order filledOrder1, Order filledOrder2, Integer fillPrice, Integer quantity){
        UUID filledExecution1Uuid = UUID.randomUUID();
        UUID filledExecution2Uuid = UUID.randomUUID();

        Execution filledExecution1 = new Execution(filledExecution1Uuid, filledExecution2Uuid, filledOrder1, fillPrice, quantity, 0,true);
        Execution filledExecution2  = new Execution(filledExecution2Uuid, filledExecution1Uuid, filledOrder2, fillPrice, quantity, 0, true);
        executionNotifier.pushExecution(filledExecution1);
        executionNotifier.pushExecution(filledExecution2);
    }
}
