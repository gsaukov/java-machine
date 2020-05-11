package com.apps.depositary.service;

import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class DepositExecutor {

    @Value("${depositary.executorsNumber}")
    private int executorsNumber;

    @Autowired
    private ApplicationContext context;

    private ThreadPoolExecutor executor;

    public void runDepositExecutor() {
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(executorsNumber);
        for (int i = 0; i < 10; i++) {
            DepositWorker worker = context.getBean(DepositWorker.class);
            worker.setNameDepositPersister(i);
            executor.execute(worker);
        }
    }

    public int getConsistentHash(UUID id) {
        return Hashing.consistentHash(Hashing.goodFastHash(32).hashString(id.toString(), Charset.forName("US-ASCII")), executorsNumber);
    }
}
