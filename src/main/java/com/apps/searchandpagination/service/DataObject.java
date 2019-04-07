package com.apps.searchandpagination.service;

import com.apps.alertserver.Alert;

import java.util.UUID;

public class DataObject {

    private final String id = UUID.randomUUID().toString();
    private String symbol;
    private String account;
    private Route route;
    private int val;

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getAccount() {
        return account;
    }

    public Route getRoute() {
        return route;
    }

    public int getVal() {
        return val;
    }

    public static enum Route {
        BUY,
        SELL;
    }

}
