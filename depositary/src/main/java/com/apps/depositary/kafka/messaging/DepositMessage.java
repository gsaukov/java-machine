package com.apps.depositary.kafka.messaging;

import java.util.Date;
import java.util.UUID;

//Overnight/Existing position of an account in deposit source for position.
public class DepositMessage {

    private UUID depositUuid;
    private Date timestamp;
    private String symbol;
    private String accountId;
    private String route;
    private Integer fillPrice;
    private Integer blockedPrice;
    private Integer quantity;

    //used to create position from deposit
    public DepositMessage() {}

    public UUID getDepositUuid() {
        return depositUuid;
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

    public String getRoute() {
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
