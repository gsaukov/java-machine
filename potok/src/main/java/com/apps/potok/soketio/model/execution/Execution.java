package com.apps.potok.soketio.model.execution;

import java.io.Serializable;
import java.util.UUID;

public class Execution implements Serializable {

    private final UUID orderUuid;
    private final String accountId;
    private final Integer fillPrice;
    private final Integer quantity;
    private final boolean filled;

    public Execution (UUID orderUuid, String accountId, Integer fillPrice, Integer quantity, boolean filled) {
        this.orderUuid = orderUuid;
        this.accountId = accountId;
        this.fillPrice = fillPrice;
        this.quantity = quantity;
        this.filled = filled;
    }

    public UUID getOrderUuid() {
        return orderUuid;
    }

    public String getAccountId() {
        return accountId;
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
