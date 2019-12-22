package com.apps.potok.exchange.core;

import com.apps.potok.soketio.model.execution.Execution;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static com.apps.potok.exchange.core.Route.BUY;

@Deprecated // working but blocking implementation
public class PositionV1 {

    private final ReentrantLock lock;

    private final UUID uuid;
    private final Date createdTimestamp;
    private final String symbol;
    private final String account;
    private final AtomicDouble weightedAveragePrice;
    private final AtomicInteger volume;
    private final ConcurrentHashMap<UUID, UUID> buyOriginatingOrders;
    private final ConcurrentHashMap<UUID, Execution> buyExecutions;
    private final ConcurrentHashMap<UUID, UUID> sellOriginatingOrders;
    private final ConcurrentHashMap<UUID, Execution> sellExecutions;

    public PositionV1(Execution execution) {
        lock = new ReentrantLock();

        this.uuid = UUID.randomUUID();
        this.createdTimestamp = new Date();
        this.symbol = execution.getSymbol();
        this.account = execution.getAccountId();
        this.weightedAveragePrice = new AtomicDouble(execution.getFillPrice());
        this.volume = new AtomicInteger(execution.getQuantity());
        this.buyOriginatingOrders = new ConcurrentHashMap<>();
        this.buyOriginatingOrders.put(execution.getOrderUuid(), execution.getOrderUuid());
        this.buyExecutions = new ConcurrentHashMap<>();
        this.buyExecutions.put(execution.getUuid(), execution);
        this.sellOriginatingOrders = new ConcurrentHashMap<>();
        this.sellExecutions = new ConcurrentHashMap<>();
    }

    public void applyExecution (Execution execution) {
        if(!buyExecutions.containsKey(execution) && !sellExecutions.containsKey(execution)) {
            if(BUY.equals(execution.getRoute())) {
                applyBuyExecution(execution);
            } else {
                applySellExecution(execution);
            }
        }
    }

    private void applyBuyExecution (Execution execution) {
        applyBuyWeightedAveragePriceAndVolume(execution);
        buyOriginatingOrders.put(execution.getOrderUuid(), execution.getOrderUuid());
        buyExecutions.put(execution.getUuid(), execution);
    }

    // Cant use compare and set since two  2 values volume and weightedAveragePrice has to be rewritten.
    // WeightedAveragePrice and volume could be packed into one immutable object and it shall be compared and swapped but that may produce garbage.
    // Will see.
    private void applyBuyWeightedAveragePriceAndVolume(Execution execution) {
        double prev, next, existingWeight, newWeight, newVolume;
        lock.lock();
        try {
            prev = weightedAveragePrice.get();
            existingWeight = prev * volume.get();
            newWeight = execution.getFillPrice() * execution.getQuantity();
            newVolume = volume.get() + execution.getQuantity() ;
            next = (existingWeight + newWeight) / newVolume;
            weightedAveragePrice.set(next);
            volume.getAndAdd(execution.getQuantity());
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // just lock so you can safely change volume otherwise it may affect applyBuyWeightedAveragePriceAndVolume.
    private void applySellExecution (Execution execution) {
        lock.lock();
        try {
            volume.getAndAdd(-execution.getQuantity()); //decrement
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        sellOriginatingOrders.put(execution.getOrderUuid(), execution.getOrderUuid());
        sellExecutions.put(execution.getUuid(), execution);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getAccount() {
        return account;
    }

    public Double getWeightedAveragePrice() {
        return weightedAveragePrice.get();
    }

    public Integer getVolume() {
        return volume.get();
    }

    public ConcurrentHashMap<UUID, UUID> getBuyOriginatingOrders() {
        return buyOriginatingOrders;
    }

    public ConcurrentHashMap<UUID, Execution> getBuyExecutions() {
        return buyExecutions;
    }

    public ConcurrentHashMap<UUID, UUID> getSellOriginatingOrders() {
        return sellOriginatingOrders;
    }

    public ConcurrentHashMap<UUID, Execution> getSellExecutions() {
        return sellExecutions;
    }

}
