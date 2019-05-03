package com.apps.searchandpagination.persistance.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Access(AccessType.FIELD)
@Table(name = "MK_DATA")
public class MkData {

    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "SYMBOL")
    private String symbol;
    @Column(name = "ACCOUNT")
    private String account;
    @Enumerated(EnumType.STRING)
    @Column(name = "ROUTE")
    private Route route;
    @Column(name = "VAL")
    private int val;

    public MkData() {
    }

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