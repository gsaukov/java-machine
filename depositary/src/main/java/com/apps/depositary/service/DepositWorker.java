package com.apps.depositary.service;

import com.apps.depositary.persistance.entity.Deposit;
import com.apps.depositary.persistance.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class DepositWorker extends AbstractDepositaryWorker {

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;

    private final AtomicLong i = new AtomicLong();
    private final ConcurrentLinkedDeque<SafeDeposit> eventQueue = new ConcurrentLinkedDeque<>();
    private ArrayList<SafeDeposit> persistBatch = new ArrayList<>();
    private ArrayList<SafeDeposit> updateBatch = new ArrayList<>();


    @Autowired
    private DepositRepository depositRepository;

    public void setNameDepositPersister(int num) {
        super.setName("DepositaryPersister_" + num);
    }

    @Override
    public void runDepositaryWorker() {
        SafeDeposit deposit = eventQueue.poll();
        if(deposit != null){

            if(deposit.isPersisted()){
                updateBatch.add(deposit);
            } else {
                persistBatch.add(deposit);
            }

            if(persistBatch.size() >= batchSize && i.get() % batchSize == 0){
                depositRepository.saveAll(persistBatch.stream().map(this::toNewDeposit).collect(Collectors.toList()));
                persistBatch.stream().map(d -> d.setPersisted());
                persistBatch = new ArrayList<>();
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
