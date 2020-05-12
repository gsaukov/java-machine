package com.apps.depositary.service;

import com.apps.depositary.persistance.entity.Deposit;
import com.apps.depositary.persistance.repository.DepositRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class DepositWorker extends AbstractDepositaryWorker {

    private final Log logger = LogFactory.getLog(DepositWorker.class);

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private int batchSize;


    private final ConcurrentLinkedDeque<SafeDeposit> eventQueue = new ConcurrentLinkedDeque<>();
    private Set<SafeDeposit> persistBatch = new HashSet<>();
    private Set<SafeDeposit> updateBatch = new HashSet<>();

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
            tryPersistBatch();
            tryUpdateBatch();
        } else {
            speedControl();
        }
    }

    private void tryPersistBatch() {
        if(persistBatch.size() >= batchSize && persistBatch.size() % batchSize == 0){
            logger.info(this.getName() + " persisting " + persistBatch.size() + " deposits");
            depositRepository.saveAll(persistBatch.stream().map(this::toNewDeposit).collect(Collectors.toList()));
            persistBatch.stream().forEach(d -> d.setPersisted());
            logger.info(this.getName() + " persisting deposits finished");
            persistBatch = new HashSet<>();
        }
    }

    private void tryUpdateBatch() {
        if(updateBatch.size() >= batchSize && updateBatch.size() % batchSize == 0){
            logger.info(this.getName() + " updating " + updateBatch.size() + " deposits");
            for(SafeDeposit deposit : updateBatch){
                depositRepository.updateDeposit(deposit.getFillPrice().get(), deposit.getQuantity().get(), deposit.getUuid());
            }
            logger.info(this.getName() + " update deposits finished");
            updateBatch = new HashSet<>();
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
