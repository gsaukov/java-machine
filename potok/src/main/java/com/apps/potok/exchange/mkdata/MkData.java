package com.apps.potok.exchange.mkdata;

public class MkData {

    private final String symbol;
    private final String account;
    private final Route route;
    private final Integer val;
    private final Integer volume;

    public MkData(String symbol, String account, Route route, Integer val, Integer volume) {
        this.symbol = symbol;
        this.account = account;
        this.route = route;
        this.val = val;
        this.volume = volume;
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

    public String getAccount() {
        return account;
    }

    public Integer getVolume() {
        return volume;
    }

}
