package com.apps.potok.server.mkdata;

public class MkData {


    private final String symbol;
    private final int val;
    private final Route route;

    public MkData(String symbol, int val, Route route) {
        this.symbol = symbol;
        this.val = val;
        this.route = route;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getVal() {
        return val;
    }

    public Route getRoute() {
        return route;
    }
}
