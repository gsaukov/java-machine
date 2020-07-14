package com.apps.finapi.result;


import org.testng.annotations.Test;

import static org.testng.Assert.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.apps.finapi.result.InstantiationCounter.createInstance;
import static com.apps.finapi.result.InstantiationCounter.getInstanceCount;

public class InstantiationCounterTest {

    private static final int ITERATIONS = 10000;
    private static final int THREADS = 10;
    private static final long AWAIT = 5l;

    @Test
    public void testInstantiationCount() throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADS);
        for (int i = 1; i <= THREADS; i++) {
            TestThread thread = new TestThread(ITERATIONS);
            executor.execute(thread);
        }
        executor.awaitTermination(AWAIT, TimeUnit.SECONDS);

        assertEquals(getInstanceCount(), ITERATIONS * THREADS);
    }

    private class TestThread extends Thread {

        private final int n;

        TestThread(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            for (int i = 0; i < n; i++) {
                createInstance();
            }
        }
    }
}


