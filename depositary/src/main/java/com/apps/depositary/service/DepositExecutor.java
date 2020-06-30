package com.apps.depositary.service;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class DepositExecutor {

    private static final Charset CHARSET = Charset.forName("US-ASCII");
    private static final int HASH_BITS = 32;

    @Autowired
    private ApplicationContext context;

    private final ThreadPoolExecutor executor;
    private final int executorsNumber;
    private final AtomicBoolean initialized;
    private final List<DepositWorker> workers = new ArrayList<>();

    private DepositExecutor(@Value("${depositary.executorsNumber}") int executorsNumber) {
        this.executorsNumber = executorsNumber;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(executorsNumber);
        this.initialized = new AtomicBoolean(false);
    }

    public void runDepositExecutor() {
        if(!initialized.get()){
            for (int i = 0; i < executorsNumber; i++) {
                DepositWorker worker = context.getBean(DepositWorker.class);
                worker.setNameDepositPersister(i);
                workers.add(worker);
                executor.execute(worker);
            }
        }
        initialized.getAndSet(true);
    }

    public void executeDeposit(SafeDeposit deposit){
        workers.get(getConsistentHash(deposit.getUuid())).persist(deposit);
    }

    private int getConsistentHash(UUID id) {
        HashCode hash = Hashing.goodFastHash(HASH_BITS).hashString(id.toString(), CHARSET);
        return Hashing.consistentHash(hash, executorsNumber);
    }
}
