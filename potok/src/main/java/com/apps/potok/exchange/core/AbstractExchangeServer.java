package com.apps.potok.exchange.core;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicBoolean;


public abstract class AbstractExchangeServer extends Thread {

    @Autowired
    protected ExchangeSpeed exchangeSpeed;

    private final AtomicBoolean running = new AtomicBoolean(true);

    @Override
    public void run() {
        while(running.get() && !Thread.currentThread().isInterrupted()){
            speedControl();
            runExchangeServer();
        }
    }

    public void stopExchangeServer(){
        running.getAndSet(false);
    }

    public abstract void runExchangeServer();

    public abstract void speedControl();

}
