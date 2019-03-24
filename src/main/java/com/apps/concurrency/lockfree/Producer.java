package com.apps.concurrency.lockfree;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

public class Producer implements Runnable {
    private long numbersToProduce;
    private Queue<Long> task;
    private AtomicLong itemsCount;

    public Producer(long numbersToProduce, Queue<Long> task,
                    AtomicLong itemsCount) {
        this.numbersToProduce = numbersToProduce;
        this.task = task;
        this.itemsCount = itemsCount;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        long lastNumber = -1;

        for (; ; ) {
            long curr = itemsCount.get();
            if (curr >= numbersToProduce)
                break;
            if (task.offer(curr + 1)) {
                lastNumber = curr + 1;
                itemsCount.incrementAndGet();
            }
        }

//        System.out.println(this.getClass() + " via " + task.getClass()
//                + " time (ms) " + (System.currentTimeMillis() - start)
//                + " Last Number " + lastNumber);
        while (!task.offer(-1l)) ;
    }

}