package com.apps.potok.soketio.model.execution;

import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.Route;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

//Execution from exchange source for a position.
public class Execution implements Serializable, Accountable {

    private final UUID executionUuid;
    private final UUID counterExecutionUuid;
    private final UUID orderUuid;
    private final Date timestamp;
    private final String symbol;
    private final String accountId;
    private final Route route;
    private final Integer fillPrice;
    private final Integer blockedPrice;
    private final Integer quantity;
    private final Integer orderLeftQuantity;
    private final boolean filled;

    //Order passed here only for reference final information such is orderId//symbol/accountId/route/blockedprice do not do any computation here.
    public Execution (UUID executionUuid, UUID counterExecutionUuid, Order order, Integer fillPrice, Integer quantity, Integer orderLeftQuantity, boolean filled) {
        this.executionUuid = executionUuid;
        this.counterExecutionUuid = counterExecutionUuid;
        this.orderUuid = order.getUuid();
        this.timestamp = new Date();
        this.symbol = order.getSymbol();
        this.accountId = order.getAccount();
        this.route = order.getRoute();
        this.fillPrice = fillPrice;
        this.blockedPrice = order.getBlockedPrice();
        this.quantity = quantity;
        this.orderLeftQuantity = orderLeftQuantity;
        this.filled = filled;
    }

    @Override
    public UUID getUuid() {
        return executionUuid;
    }

    public UUID getCounterExecutionUuid() {
        return counterExecutionUuid;
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

    public Integer getOrderLeftQuantity() {
        return orderLeftQuantity;
    }

    @Override
    public String toString() {
        return "Execution{" +
                "executionUuid=" + executionUuid +
                ", counterExecutionUuid=" + counterExecutionUuid +
                ", orderUuid=" + orderUuid +
                ", timestamp=" + timestamp +
                ", symbol='" + symbol + '\'' +
                ", accountId='" + accountId + '\'' +
                ", route=" + route +
                ", fillPrice=" + fillPrice +
                ", blockedPrice=" + blockedPrice +
                ", quantity=" + quantity +
                ", filled=" + filled +
                '}';
    }
}
