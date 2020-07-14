package com.apps.finapi.result;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Class that keeps track of how many instances of the class have been created during runtime.
 * It prints the current count of created instances to console whenever a new instance is being created.
 * Counter is thread safe.
 */

public class InstantiationCounter {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    private InstantiationCounter() {}

    public static InstantiationCounter createInstance() {
        System.out.println(COUNTER.incrementAndGet());
        return new InstantiationCounter();
    }

    public static long getInstanceCount() {
        return COUNTER.get();
    }
}
