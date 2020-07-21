package com.apps.finapi.result;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class TestProducer extends Thread {

    private static final AtomicLong COUNTER = new AtomicLong(0);
    private final Pipeline pipeline;
    private final Map map;
    private final int n;

    public TestProducer(Pipeline pipeline, Map map, int n) {
        this.pipeline = pipeline;
        this.map = map;
        this.n = n;
    }

    @Override
    public void run() {
        for (int i = 0; i < n; i++) {
            try {
                Object obj = COUNTER.incrementAndGet();
                map.put(obj, obj);
                pipeline.put(obj);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
