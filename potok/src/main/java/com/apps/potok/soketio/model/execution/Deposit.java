package com.apps.potok.soketio.model.execution;

import com.apps.potok.exchange.core.Route;

import java.util.Date;
import java.util.UUID;

//Overnight/Existing position of an account in deposit source for position.
public class Deposit implements Accountable {

    private final UUID depositUuid;
    private final Date timestamp;
    private final String symbol;
    private final String accountId;
    private final Route route;
    private final Integer fillPrice;
    private final Integer blockedPrice;
    private final Integer quantity;

    //used to create position from deposit
    public Deposit(UUID depositUuid, Date timestamp, String symbol, String accountId, Route route, Integer fillPrice, Integer blockedPrice, Integer quantity) {
        this.depositUuid = depositUuid;
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.accountId = accountId;
        this.route = route;
        this.fillPrice = fillPrice;
        this.blockedPrice = blockedPrice;
        this.quantity = quantity;
    }

    @Override
    public UUID getUuid() {
        return depositUuid;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
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

    @Override
    public boolean isDeposit() {
        return true;
    }
}
