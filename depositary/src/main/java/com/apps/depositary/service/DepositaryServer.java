package com.apps.depositary.service;

import com.apps.depositary.persistance.entity.Execution;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DepositaryServer extends AbstractDepositaryServer {

    private final ConcurrentLinkedDeque<Execution> eventQueue = new ConcurrentLinkedDeque<>();
    //  account|symbol|deposit(BUY,SHORT)
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, DepositContainer>> deposits = new ConcurrentHashMap<>();

    public DepositaryServer() {
        super.setName("DepositaryServer");
    }

    @Override
    public void runDepositaryServer() {
        Execution execution = eventQueue.poll();
        if(execution != null){
            addExecution(execution);
        } else {
            speedControl();
        }
    }

    public void addExecution(Execution execution) {
        DepositContainer depositContainer = getDepositContainer(execution);
        depositContainer.applyExecution(execution);
    }

    private DepositContainer getDepositContainer(Execution execution) {
        ConcurrentHashMap<String, DepositContainer> accountPositions = deposits.get(execution.getAccountId());
        DepositContainer depositContainer;
        if(accountPositions != null){
            depositContainer = accountPositions.get(execution.getSymbol());
            if(depositContainer == null) {
                depositContainer = insertDepositContainer(accountPositions, execution);
            }
        } else {
            depositContainer = insertAccountPositions(execution);
        }
        return depositContainer;
    }

    private DepositContainer insertAccountPositions(Execution execution) {
        final ConcurrentHashMap<String, DepositContainer> newAccountPositions = new ConcurrentHashMap<>();
        final ConcurrentHashMap<String, DepositContainer> existingAccountPositions = deposits.putIfAbsent(execution.getAccountId(), newAccountPositions);
        if(existingAccountPositions == null){
            return insertDepositContainer(newAccountPositions, execution);
        } else {
            return insertDepositContainer(existingAccountPositions, execution);
        }
    }

    private DepositContainer insertDepositContainer(ConcurrentHashMap<String, DepositContainer> accountPositions, Execution execution) {
        final DepositContainer newDeposit = new DepositContainer();
        final DepositContainer existingDeposit = accountPositions.putIfAbsent(execution.getSymbol(), newDeposit);
        if(existingDeposit == null){
            return newDeposit;
        } else {
            return existingDeposit;
        }
    }

    @Override
    public void speedControl() {}

}
