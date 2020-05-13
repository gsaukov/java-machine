package com.apps.depositary.persistance.entity;

import com.apps.depositary.service.Route;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Access(AccessType.FIELD)
@Table(name = "EXECUTION")
public class Execution {

    @Id
    @Type(type="uuid-char")
    @Column(name = "UUID")
    private UUID uuid;

    @Type(type="uuid-char")
    @Column(name = "COUNTER_EXECUTION_UUID")
    private UUID counterExecutionUuid;

    @Type(type="uuid-char")
    @Column(name = "ORDER_UUID")
    private UUID orderUuid;

    @Column(name = "CREATED_AT")
    private Date timestamp;

    @Column(name = "SYMBOL")
    private String symbol;

    @Column(name = "ACCOUNT_ID")
    private String accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROUTE")
    private Route route;

    @Column(name = "FILL_PRICE")
    private Integer fillPrice;

    @Column(name = "BLOCKED_PRICE")
    private Integer blockedPrice;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "FILLED")
    private boolean filled;

    @Type(type="uuid-char")
    @Column(name = "DEPOSIT_UUID")
    private UUID depositId;

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

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
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

    public UUID getDepositId() {
        return depositId;
    }

    public void setDepositId(UUID depositId) {
        this.depositId = depositId;
    }
}
