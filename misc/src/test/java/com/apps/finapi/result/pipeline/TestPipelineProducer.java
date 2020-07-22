package com.apps.finapi.result.pipeline;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

public class TestPipelineProducer implements Callable<Long> {

    private static final AtomicLong COUNTER = new AtomicLong(0);
    private final Pipeline pipeline;
    private final Map map;
    private final int n;

    public TestPipelineProducer(Pipeline pipeline, Map map, int n) {
        this.pipeline = pipeline;
        this.map = map;
        this.n = n;
    }

    @Override
    public Long call() {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            try {
                Object obj = COUNTER.incrementAndGet();
                map.put(obj, obj);
                pipeline.put(obj);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return System.nanoTime() - start;
    }
}
