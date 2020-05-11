package com.apps.depositary.service;

import com.apps.depositary.persistance.entity.Deposit;
import com.apps.depositary.persistance.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

public class DepositWorker extends AbstractDepositaryServer {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private final AtomicLong i = new AtomicLong();
    private final ConcurrentLinkedDeque<SafeDeposit> eventQueue = new ConcurrentLinkedDeque<>();
    private ArrayList<Deposit> batch = new ArrayList<>();

    @Autowired
    private DepositRepository depositRepository;

    public void setNameDepositPersister(int num) {
        super.setName("DepositaryPersister_" + num);
    }

    @Override
    public void runDepositaryServer() {
        SafeDeposit deposit = eventQueue.poll();
        if(deposit != null){
            batch.add(toNewDeposit(deposit));
            if(batch.size() >= batchSize && i.get() % batchSize == 0){
                depositRepository.saveAll(batch);
                batch = new ArrayList<>();
            }
        } else {
            speedControl();
        }
    }

    private Deposit toNewDeposit(SafeDeposit deposit) {
        Deposit newDeposit = new Deposit();
        newDeposit.setUuid(deposit.getUuid());
        newDeposit.setTimestamp(deposit.getTimestamp());
        newDeposit.setSymbol(deposit.getSymbol());
        newDeposit.setAccountId(deposit.getAccountId());
        newDeposit.setRoute(deposit.getRoute());
        newDeposit.setFillPrice(deposit.getFillPrice().get());
        newDeposit.setBlockedPrice(deposit.getBlockedPrice().get());
        newDeposit.setQuantity(deposit.getQuantity().get());

        return newDeposit;
    }

    public void persist(SafeDeposit deposit){
        eventQueue.add(deposit);
    }

    @Override
    public void speedControl() {

    }


}
