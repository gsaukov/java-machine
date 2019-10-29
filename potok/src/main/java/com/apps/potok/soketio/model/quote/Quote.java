package com.apps.potok.soketio.model.quote;

import java.io.Serializable;

public class Quote implements Serializable {

    private final String symbol;
    private final Integer price;

    public Quote(String symbol, Integer price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public Integer getPrice() {
        return price;
    }

}
