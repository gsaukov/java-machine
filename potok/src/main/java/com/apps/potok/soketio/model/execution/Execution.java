package com.apps.potok.soketio.model.execution;

import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.mkdata.Route;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import static java.util.UUID.randomUUID;

//Execution from exchange source for a position.
public class Execution implements Serializable, Accountable {

    private final UUID executionUuid;
    private final UUID orderUuid;
    private final Date timestamp;
    private final String symbol;
    private final String accountId;
    private final Route route;
    private final Integer fillPrice;
    private final Integer blockedPrice;
    private final Integer quantity;
    private final boolean filled;

    public Execution (Order order, Integer fillPrice, Integer quantity, boolean filled) {
        this.executionUuid = randomUUID();
        this.timestamp = new Date();
        this.orderUuid = order.getUuid();
        this.symbol = order.getSymbol();
        this.accountId = order.getAccount();
        this.route = order.getRoute();
        this.fillPrice = fillPrice;
        this.blockedPrice = order.getBlockedPrice();
        this.quantity = quantity;
        this.filled = filled;
    }

    @Override
    public UUID getUuid() {
        return executionUuid;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    public UUID getOrderUuid() {
        return orderUuid;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getAccountId() {
        return accountId;
    }

    @Override
    public Route getRoute() {
        return route;
    }

    @Override
    public Integer getFillPrice() {
        return fillPrice;
    }

    @Override
    public Integer getBlockedPrice() {
        return blockedPrice;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    public boolean isFilled() {
        return filled;
    }

    @Override
    public boolean isDeposit() {
        return false;
    }
}
