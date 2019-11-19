package com.apps.potok.exchange.core;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.apps.potok.exchange.mkdata.Route.BUY;

@Service
public class OrderManager {

    private final ConcurrentHashMap<UUID, Order> orderPool;
    private final AskContainer askContainer;
    private final BidContainer bidContainer;

    public OrderManager(BidContainer bidContainer, AskContainer askContainer) {
        this.askContainer = askContainer;
        this.bidContainer = bidContainer;
        this.orderPool = new ConcurrentHashMap();
    }

    public void addOrder(Order order) {
        orderPool.put(order.getUuid(), order);
    }

    // returns removed order, returns null if order is already executed or not found.
    public Order cancelOrder(UUID uuid, String accountId) {
        Order orderToRemove = orderPool.remove(uuid);
        if (orderToRemove != null && orderToRemove.getAccount().equals(accountId)){
            if(BUY.equals(orderToRemove.getRoute())){
                if(askContainer.removeAsk(orderToRemove)){
                    return orderToRemove;
                }
            } else {
                if (bidContainer.removeBid(orderToRemove)) {
                    return orderToRemove;
                }
            }
        }
        return null;
    }

    public Order executeOrder(UUID uuid, String accountId) {
        // should be done for down stream processing persistance, accounting, transaction journalization.
        return orderPool.remove(uuid);
    }

}
