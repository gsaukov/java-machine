package com.apps.potok.soketio.model.execution;

import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.mkdata.Route;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class Execution implements Serializable {

    private final UUID executionUuid;
    private final UUID orderUuid;
    private final Date timestamp;
    private final String symbol;
    private final String accountId;
    private final Route route;
    private final Integer fillPrice;
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
        this.quantity = quantity;
        this.filled = filled;
    }

    public UUID getExecutionUuid() {
        return executionUuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public UUID getOrderUuid() {
        return orderUuid;
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

    public Integer getFillPrice() {
        return fillPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public boolean isFilled() {
        return filled;
    }

}
