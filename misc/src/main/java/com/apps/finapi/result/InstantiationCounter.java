package com.apps.finapi.result;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO:
 * Write a class that keeps track of how many instances of the class have been created during runtime.
 * It should print the current count of created instances to console whenever a new instance is being created.
 *
 * NOTE: Must work in a multi-threaded environment.
 */

public class InstantiationCounter {

    private static final AtomicLong counter = new AtomicLong(0);

    private InstantiationCounter(){}

    public static InstantiationCounter createInstance() {
        System.out.println(counter.incrementAndGet());
        return new InstantiationCounter();
    }
}
