package com.apps.depositary.service;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import static com.apps.depositary.service.Route.SELL;

public class SafeDeposit {

    private final ReentrantLock lock;

    private final UUID uuid;
    private final Date timestamp;
    private final String symbol;
    private final String accountId;
    private final Route route;
    private final AtomicDouble fillPrice;
    private final AtomicInteger blockedPrice;
    private final AtomicInteger quantity;
    private final AtomicBoolean persisted;

    public SafeDeposit(UUID uuid, Date timestamp, String symbol, String accountId, Route route, Double fillPrice,
                       Integer blockedPrice, Integer quantity, boolean persisted) {
        this.lock = new ReentrantLock();
        
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.accountId = accountId;
        this.route = route;
        this.fillPrice = new AtomicDouble(fillPrice);
        this.blockedPrice = new AtomicInteger(blockedPrice);
        this.quantity = new AtomicInteger(quantity);
        this.persisted = new AtomicBoolean(persisted);
    }

    public void applyExecution (SafeExecution execution) {
        if(SELL.equals(execution.getRoute())) {
            applySellExecution(execution);
        } else {
            applyBuyShortExecution(execution);
        }
    }

    private void applyBuyShortExecution(SafeExecution execution) {
        applyBuyWeightedAveragePriceAndVolume(execution);
    }

    private void applyBuyWeightedAveragePriceAndVolume(SafeExecution execution) {
        double prev, next, existingWeight, newWeight, newQuantity;
        lock.lock();
        try {
            prev = fillPrice.get();
            existingWeight = prev * quantity.get();
            newWeight = execution.getFillPrice() * execution.getQuantity();
            newQuantity = quantity.get() + execution.getQuantity() ;
            if(newQuantity > 0){ //NaN safety
                next = (existingWeight + newWeight) / newQuantity;
                fillPrice.set(next);
            }

            quantity.getAndAdd(execution.getQuantity());
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void applySellExecution (SafeExecution execution) {
        lock.lock();
        try {
            quantity.getAndAdd(-execution.getQuantity());
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getAccountId() {
        return accountId;
    }

    public Route getRoute() {
        return route;
    }

    public AtomicDouble getFillPrice() {
        return fillPrice;
    }

    public AtomicInteger getBlockedPrice() {
        return blockedPrice;
    }

    public AtomicInteger getQuantity() {
        return quantity;
    }

    public boolean setPersisted() {
        return persisted.getAndSet(true);
    }

    public boolean isPersisted() {
        return persisted.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SafeDeposit deposit = (SafeDeposit) o;
        return uuid.equals(deposit.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
