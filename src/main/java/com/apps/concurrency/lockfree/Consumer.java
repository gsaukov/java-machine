package com.apps.concurrency.lockfree;

import java.util.Queue;

class Consumer implements Runnable {

    private Queue<Long> task;

    public Consumer(Queue<Long> task) {
        this.task = task;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        long lastValue = -1;

        for (; ; ) {
            {
                Long value = task.poll();
                if (value != null) {
                    if (value == -1) {
                        while (!task.offer(-1l)) ;
                        break;
                    }
                    lastValue = value;
                }


            }

        }

//        System.out.println(this.getClass() + " Via " + task.getClass()
//                + " time (ms) " + (System.currentTimeMillis() - start)
//                + " Last Value " + lastValue);
    }

}
