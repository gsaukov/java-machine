package com.apps.potok.soketio.model.execution;

public class CloseShortPositionRequest {

    private String symbol;
    private Integer amount;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
