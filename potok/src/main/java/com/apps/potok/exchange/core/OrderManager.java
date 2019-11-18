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

    public boolean removeOrder(UUID uuid, String accountId) {
        Order orderToRemove = orderPool.remove(uuid);
        if (orderToRemove != null && orderToRemove.getAccount().equals(accountId)){
            if(BUY.equals(orderToRemove.getRoute())){
                return askContainer.removeAsk(orderToRemove);
            } else {
                return bidContainer.removeBid(orderToRemove);
            }
        } else {
            return false;
        }
    }

    public void executeOrder(UUID uuid, String accountId) {
        // should be done for down stream processing persistance, accounting, transaction journalization.
        orderPool.remove(uuid);
    }

}
