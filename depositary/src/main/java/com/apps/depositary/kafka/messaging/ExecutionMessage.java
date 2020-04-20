package com.apps.depositary.kafka.messaging;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

//Execution from exchange source for a position.
public class ExecutionMessage implements Serializable {

    private UUID uuid;
    private UUID counterExecutionUuid;
    private UUID orderUuid;
    private Date timestamp;
    private String symbol;
    private String accountId;
    private String route;
    private Integer fillPrice;
    private Integer blockedPrice;
    private Integer quantity;
    private boolean filled;

    public ExecutionMessage() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getCounterExecutionUuid() {
        return counterExecutionUuid;
    }

    public void setCounterExecutionUuid(UUID counterExecutionUuid) {
        this.counterExecutionUuid = counterExecutionUuid;
    }

    public UUID getOrderUuid() {
        return orderUuid;
    }

    public void setOrderUuid(UUID orderUuid) {
        this.orderUuid = orderUuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Integer getFillPrice() {
        return fillPrice;
    }

    public void setFillPrice(Integer fillPrice) {
        this.fillPrice = fillPrice;
    }

    public Integer getBlockedPrice() {
        return blockedPrice;
    }

    public void setBlockedPrice(Integer blockedPrice) {
        this.blockedPrice = blockedPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    @Override
    public String toString() {
        return "Execution{" +
                "executionUuid=" + uuid +
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
