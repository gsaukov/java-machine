package com.apps.depositary.service;

import com.apps.depositary.persistance.entity.Execution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//ThreadSafe
@Service
public class DepositaryService{

    @Autowired
    private DepositExecutor depositExecutor;

    //  account|symbol|deposit(BUY,SHORT)
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, DepositContainer>> deposits = new ConcurrentHashMap<>();

    public void processExecution(Execution execution) {
        DepositContainer depositContainer = getDepositContainer(execution);
        SafeDeposit deposit = depositContainer.applyExecution(execution);
        depositExecutor.executeDeposit(deposit);
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
}
