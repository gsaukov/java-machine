package com.apps.potok.exchange.core;

import com.apps.potok.exchange.mkdata.Route;
import com.apps.potok.soketio.server.Account;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

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
        Order orderToRemove = orderPool.get(uuid);
        if (orderToRemove != null && orderToRemove.getAccount().equals(accountId)){
            orderToRemove.cancel();
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

    public Order executeOrder(UUID uuid, Account accountId) {
        // should be done for down stream processing persistance, accounting, transaction journalization and balance update.
        return orderPool.get(uuid);
    }

    public long getCancelled(Route route){
        AtomicLong res = new AtomicLong(0l);
        for(Order order : orderPool.values()){
            if(!order.isActive() && order.getRoute().equals(route)) {
                res.getAndAdd(order.getVolume());
            }
        }
        return res.get();
    }
 }
