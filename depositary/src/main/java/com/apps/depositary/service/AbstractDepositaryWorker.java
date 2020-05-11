package com.apps.depositary.service;

import java.util.concurrent.atomic.AtomicBoolean;


public abstract class AbstractDepositaryWorker extends Thread {

    private final AtomicBoolean running = new AtomicBoolean(true);

    @Override
    public void run() {
        while(running.get() && !Thread.currentThread().isInterrupted()){
            speedControl();
            runDepositaryWorker();
        }
    }

    public void stopDepositaryWorker(){
        running.getAndSet(false);
    }

    public abstract void runDepositaryWorker();

    public abstract void speedControl();

}
