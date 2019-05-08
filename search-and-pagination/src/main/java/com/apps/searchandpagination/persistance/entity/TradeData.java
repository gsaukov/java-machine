package com.apps.searchandpagination.persistance.entity;

import org.joda.money.BigMoney;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Access(AccessType.FIELD)
@Table(name = "TRADE_DATA")
public class TradeData {

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
    @Embedded
    @AttributeOverrides(
            { @AttributeOverride(name = "amount", column = @Column(name = "AMOUNT")),
              @AttributeOverride(name = "currency", column = @Column(name = "CURRENCY")) })
    private BigMoney amount;
        @Column(name = "DATE")
    private LocalDateTime date;

    public TradeData() {
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

    public BigMoney getAmount() {
        return amount;
    }

    public LocalDateTime getDate() {
        return date;
    }


    public static enum Route {
        BUY,
        SELL;
    }

}