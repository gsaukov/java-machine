package com.apps.depositary.service;

import com.apps.depositary.persistance.entity.Deposit;
import com.apps.depositary.persistance.entity.Execution;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DepositContainer {

    private final ConcurrentHashMap<Route, SafeDeposit> container;

    public DepositContainer() {
        this.container = new ConcurrentHashMap<>();
    }

    public SafeDeposit getDeposit() {
        return container.get(Route.BUY);
    }

    //only initialLoad
    public void putDeposit(Deposit deposit) {
        container.put(Route.BUY, toExistingSafeDeposit(deposit));
    }

    public SafeDeposit getShortDeposit() {
        return container.get(Route.SHORT);
    }

    //only initialLoad
    public void putShortDeposit(Deposit shortDeposit) {
        container.put(Route.SHORT, toExistingSafeDeposit(shortDeposit));
    }

    public UUID applyExecution(Execution execution) {
        final SafeDeposit newDeposit = toNewSafeDeposit(execution);
        final SafeDeposit existingDeposit = container.putIfAbsent(newDeposit.getRoute(), newDeposit);
        if(existingDeposit == null){
            return newDeposit.getUuid();
        } else {
            existingDeposit.applyExecution(toNewSafeExecution(execution));
            return existingDeposit.getUuid();
        }
    }

    private SafeDeposit toExistingSafeDeposit(Deposit deposit) {
        return new SafeDeposit(deposit.getUuid(), deposit.getTimestamp(), deposit.getSymbol(), deposit.getAccountId(),
                deposit.getRoute(), deposit.getFillPrice(), deposit.getBlockedPrice(), deposit.getQuantity());
    }

    private SafeDeposit toNewSafeDeposit(Execution execution) {
        return new SafeDeposit(UUID.randomUUID(), execution.getTimestamp(), execution.getSymbol(), execution.getAccountId(),
                Route.valueOf(execution.getRoute()), execution.getFillPrice().doubleValue(), execution.getBlockedPrice(), execution.getQuantity());
    }

    private SafeExecution toNewSafeExecution(Execution execution) {
        return new SafeExecution(execution.getUuid(),  execution.getSymbol(), execution.getAccountId(),
                Route.valueOf(execution.getRoute()), execution.getFillPrice(), execution.getBlockedPrice(), execution.getQuantity());
    }
}
