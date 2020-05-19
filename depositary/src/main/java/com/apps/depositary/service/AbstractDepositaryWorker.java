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

    public void speedControl(long timeout) {
        if (timeout > 0) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (timeout == 0) {
            //full speed
        } else {
            //negative timeout kill the thread
            Thread.currentThread().interrupted();
        }
    }

    public abstract void runDepositaryWorker();

    public abstract void speedControl();

}
