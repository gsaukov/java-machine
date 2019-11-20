package com.apps.potok.exchange.core;

import com.apps.potok.exchange.mkdata.Route;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.UUID.randomUUID;

public class Order {

    private final UUID uuid;
    private final String symbol;
    private final String account;
    private final Route route;
    private final Integer val;
    private final AtomicInteger volume;
    private final AtomicBoolean active;

    public Order(String symbol, String account, Route route, Integer val, Integer volume) {
        this.uuid = randomUUID();
        this.symbol = symbol;
        this.account = account;
        this.route = route;
        this.val = val;
        this.volume = new AtomicInteger(volume);
        this.active = new AtomicBoolean(true);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getAccount() {
        return account;
    }

    public Route getRoute() {
        return route;
    }

    public Integer getVal() {
        return val;
    }

    public Integer getVolume() {
        return volume.get();
    }

    public void partFill(Order order) {
        volume.getAndAdd(-order.getVolume()); //decrement
    }

    public void cancel() {
        active.getAndSet(false);
    }

    public boolean isActive() {
        return active.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(uuid, order.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
