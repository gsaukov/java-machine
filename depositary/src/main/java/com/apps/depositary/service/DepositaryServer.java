package com.apps.depositary.service;

import com.apps.depositary.persistance.entity.Deposit;
import com.apps.depositary.persistance.entity.Execution;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DepositaryServer extends AbstractDepositaryServer {

    private final ConcurrentLinkedDeque<Execution> eventQueue = new ConcurrentLinkedDeque<>();
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

    private void addExecution(Execution execution) {
//        ConcurrentHashMap<String, DepositContainer> accountPositions = deposits.get(execution.getAccountId());
//        if(accountPositions != null){
//            DepositContainer depositContainer = accountPositions.get(execution.getSymbol());
//            if(depositContainer != null) {
//                if(depositContainer.getDeposit()!=null) {
//                    if(isShortExecution(execution)){
//                        addShortDeposit(depositContainer, execution);
//                    } else {
//                        addDeposit();
//                    }
//                }
//                insertDepositContainer(accountPositions, execution);
//            }
//        } else {
//            insertAccountPositions(execution);
//        }
    }

    private boolean isShortExecution(Execution execution) {
        return Route.SHORT.name().equals(execution.getRoute());
    }

    @Override
    public void speedControl() {}

}
