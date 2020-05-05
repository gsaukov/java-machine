package com.apps.depositary.service;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


public class SafeExecution {

    private final UUID uuid;
    private final String symbol;
    private final String accountId;
    private final Route route;
    private final Integer fillPrice;
    private final Integer blockedPrice;
    private final Integer quantity;

    public SafeExecution(UUID uuid, String symbol, String accountId, Route route, Integer fillPrice, Integer blockedPrice, Integer quantity) {
        this.uuid = uuid;
        this.symbol = symbol;
        this.accountId = accountId;
        this.route = route;
        this.fillPrice = fillPrice;
        this.blockedPrice = blockedPrice;
        this.quantity = quantity;
    }

    public UUID getUuid() {
        return uuid;
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

    public Integer getBlockedPrice() {
        return blockedPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }


}
