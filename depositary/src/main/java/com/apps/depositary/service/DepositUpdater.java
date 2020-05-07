package com.apps.depositary.service;

import com.apps.depositary.persistance.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class DepositUpdater extends AbstractDepositaryServer {

    private final ConcurrentLinkedDeque<SafeDeposit> eventQueue = new ConcurrentLinkedDeque<>();

    public DepositUpdater() {
        super.setName("DepositaryUpdater");
    }

    @Autowired
    private DepositRepository depositRepository;

    @Override
    public void runDepositaryServer() {
        SafeDeposit deposit = eventQueue.poll();
        if(deposit != null){
            depositRepository.updateDeposit(deposit.getFillPrice().get(), deposit.getQuantity().get(), deposit.getUuid());
        } else {
            speedControl();
        }
    }

    public void update(SafeDeposit deposit){
        eventQueue.add(deposit);
    }

    @Override
    public void speedControl() {

    }
}
