package com.apps.depositary.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicBoolean;


public abstract class AbstractDepositaryServer extends Thread {


    private final AtomicBoolean running = new AtomicBoolean(true);

    @Override
    public void run() {
        while(running.get() && !Thread.currentThread().isInterrupted()){
            speedControl();
            runDepositaryServer();
        }
    }

    public void stopDepositaryServer(){
        running.getAndSet(false);
    }

    public abstract void runDepositaryServer();

    public abstract void speedControl();

}
