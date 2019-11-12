package com.apps.concurrency.lockfree;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

public class QueueTest {

    public static void main(String[] args) throws InterruptedException {

        long noOfItems = 100000 * 10 * 10;
        int queueSize = 100000;
        Queue<Long> task;
        int noofIte = 5;
        int noOfConsumer = 5;

        System.out.println("Start Warm Up");

        for (int x = 0; x < noofIte; x++) {
            task = new LockFreeBoundedQueue<>(queueSize);
            buildAndExecute(noOfItems, noOfConsumer, queueSize, task);
        }


        for (int x = 0; x < noofIte; x++) {
            task = new ArrayBlockingQueue<>(queueSize);
            buildAndExecute(noOfItems, noOfConsumer, queueSize, task);
        }

        System.out.println("End Warm Up");

        System.out.println("Start Test Now");
        noofIte = 2;

        for (int x = 0; x < noofIte; x++) {
            task = new LockFreeBoundedQueue<>(queueSize);
            buildAndExecute(noOfItems, noOfConsumer, queueSize, task);
        }

        for (int x = 0; x < noofIte; x++) {
            task = new ConcurrentLinkedDeque<>();
            buildAndExecute(noOfItems, noOfConsumer, queueSize, task);
        }


        for (int x = 0; x < noofIte; x++) {
            task = new ArrayBlockingQueue<>(queueSize);
            buildAndExecute(noOfItems, noOfConsumer, queueSize, task);
        }

    }

    private static void buildAndExecute(long numberOfElement, int noofCon, int boundSize, Queue<Long> task) throws InterruptedException {

        AtomicLong producerCount = new AtomicLong();
        Runnable[] consumerThread = new Runnable[noofCon];
        for (int cnt = 0; cnt < noofCon; cnt++) {
            consumerThread[cnt] = new Consumer(task);
        }

        int noofPro = 1;
        Runnable[] producerThread = new Runnable[noofPro];
        for (int cnt = 0; cnt < noofPro; cnt++) {
            producerThread[cnt] = new Producer(numberOfElement,
                    task, producerCount);
        }
        long start = System.currentTimeMillis();
        execute(numberOfElement, task, producerThread, consumerThread);
        System.out.println(" Total Time for " + task.getClass() + " \t " + (System.currentTimeMillis() - start));
        System.out.println("*****************");
    }

    private static void execute(long numberOfElement, Queue<Long> task,
                                Runnable[] producer, Runnable[] consumer)
            throws InterruptedException {

        Thread[] pThread = new Thread[producer.length];
        for (int x = 0; x < producer.length; x++) {
            pThread[x] = new Thread(producer[x]);
        }

        Thread[] conThread = new Thread[consumer.length];
        for (int x = 0; x < consumer.length; x++) {
            conThread[x] = new Thread(consumer[x]);
        }

        for (int x = 0; x < producer.length; x++) {
            pThread[x].start();
        }

        for (int x = 0; x < conThread.length; x++) {
            conThread[x].start();
        }

        for (int x = 0; x < producer.length; x++) {
            pThread[x].join();
        }

        for (int x = 0; x < consumer.length; x++) {
            conThread[x].join();
        }

    }

}

