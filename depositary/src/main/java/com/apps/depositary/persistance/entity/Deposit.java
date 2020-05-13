package com.apps.depositary.persistance.entity;

import com.apps.depositary.service.Route;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.UUID;

@Entity
@Access(AccessType.FIELD)
@Table(name = "DEPOSIT")
public class Deposit {

    @Id
    @Type(type="uuid-char")
    @Column(name = "UUID")
    private UUID uuid;

    @Column(name = "CREATED_AT")
    private Date timestamp;

    @Column(name = "SYMBOL")
    private String symbol;

    @Column(name = "ACCOUNT_ID")
    private String accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROUTE")
    private Route route;

    @Column(name = "FILL_PRICE")
    private BigDecimal fillPrice;

    @Column(name = "BLOCKED_PRICE")
    private Integer blockedPrice;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "CLOSED")
    private boolean closed;

    @Version
    @Column(name = "VERSION")
    private Long version;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public BigDecimal getFillPrice() {
        return fillPrice;
    }

    public void setFillPrice(Double fillPrice) {
        this.fillPrice = new BigDecimal(fillPrice, MathContext.DECIMAL32);
    }

    public Integer getBlockedPrice() {
        return blockedPrice;
    }

    public void setBlockedPrice(Integer blockedPrice) {
        this.blockedPrice = blockedPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }
}
