package com.apps.potok.exchange.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class BalanceCalculationStatistics {

    private Logger logger = LoggerFactory.getLogger(BalanceCalculationStatistics.class);

    private final AtomicLong accounts;
    private final AtomicLong symbols;
    private final AtomicLong orders;
    private final AtomicLong executions;
    private final AtomicLong positions;
    private final AtomicLong shortPositions;
    private final AtomicLong closeShorts;

    public BalanceCalculationStatistics() {
        this.accounts = new AtomicLong();
        this.symbols = new AtomicLong();
        this.orders = new AtomicLong();
        this.executions = new AtomicLong();
        this.positions = new AtomicLong();
        this.shortPositions = new AtomicLong();
        this.closeShorts = new AtomicLong();
    }

    public void printStatistics() {
        logger.info("BalanceCalculationStatistics{" +
                "accounts=" + accounts.get() +
                ", symbols=" + symbols.get() +
                ", orders=" + orders.get() +
                ", executions=" + executions.get() +
                ", positions=" + positions.get() +
                ", shortPositions=" + shortPositions.get() +
                ", closeShorts=" + closeShorts.get() +
                '}');
    }

    public void addAccounts(long accountsNumber) {
        accounts.addAndGet(accountsNumber);
    }

    public void addSymbols(long symbolsNumber) {
        symbols.addAndGet(symbolsNumber);
    }

    public void addOrders(long ordersNumber) {
        orders.addAndGet(ordersNumber);
    }

    public void addExecutions(long executionsNumber) {
        executions.addAndGet(executionsNumber);
    }

    public void addPositions(long positionsNumber) {
        positions.addAndGet(positionsNumber);
    }

    public void addShortPositions(long positionsNumber) {
        shortPositions.addAndGet(positionsNumber);
    }

    public void addCloseShorts(long closeShortsNumber) {
        closeShorts.addAndGet(closeShortsNumber);
    }

    public Long getAccounts() {
        return accounts.get();
    }

    public Long getSymbols() {
        return symbols.get();
    }

    public Long getOrders() {
        return orders.get();
    }

    public Long getExecutions() {
        return executions.get();
    }

    public Long getPositions() {
        return positions.get();
    }

    public Long getShortPositions() {
        return shortPositions.get();
    }

}
