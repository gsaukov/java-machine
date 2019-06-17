package com.apps.searchandpagination.persistance.entity;

import org.joda.money.BigMoney;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Access(AccessType.FIELD)
@Table(name = "TRADE_DATA")
public class TradeData {

    @Id
    @Column(name = "ID")
    private String tradeDataId;
    @Column(name = "SYMBOL")
    private String symbol;
    @Column(name = "ACCOUNT")
    private String accountId;
    @Enumerated(EnumType.STRING)
    @Column(name = "ROUTE")
    private Route route;
    @Column(name = "VAL")
    private int val;
    @Embedded
    private EmbeddableBigMoney amount;
    @Column(name = "DATE")
    private LocalDateTime date;

    public TradeData() {
    }

    public String getTradeDataId() {
        return tradeDataId;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getAccountId() {
        return accountId;
    }

    public Route getRoute() {
        return route;
    }

    public int getVal() {
        return val;
    }

    public BigMoney getAmount() {
        return BigMoney.of(amount.getCurrency(), amount.getAmount());
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setTradeDataId(String tradeDataId) {
        this.tradeDataId = tradeDataId;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public void setAmount(EmbeddableBigMoney amount) {
        this.amount = amount;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public enum Route {
        BUY,
        SELL;
    }

}