package com.apps.potok.soketio.model.execution;

import java.util.Date;
import java.util.UUID;

import static java.util.UUID.randomUUID;

public class CloseShortPosition {

    private final UUID uuid;
    private final Date timestamp;
    private final String accountId;
    private final String symbol;
    private final Integer amount;
    private UUID shortPosition;
    private UUID positivePosition;

    public CloseShortPosition (String accountId, String symbol, Integer amount) {
        this.uuid = randomUUID();
        this.timestamp = new Date();
        this.accountId = accountId;
        this.symbol = symbol;
        this.amount = amount;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getSymbol() {
        return symbol;
    }

    public Integer getAmount() {
        return amount;
    }

    public UUID getShortPosition() {
        return shortPosition;
    }

    public void setShortPosition(UUID shortPosition) {
        this.shortPosition = shortPosition;
    }

    public UUID getPositivePosition() {
        return positivePosition;
    }

    public void setPositivePosition(UUID positivePosition) {
        this.positivePosition = positivePosition;
    }
}
