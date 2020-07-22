package com.apps.finapi.result.pipeline;

import java.util.Map;
import java.util.concurrent.Callable;

import static org.testng.Assert.assertNotNull;

public class TestPipelineConsumer implements Callable<Long> {

    private final Pipeline pipeline;
    private final Map map;
    private final int n;

    public TestPipelineConsumer(Pipeline pipeline, Map map, int n) {
        this.pipeline = pipeline;
        this.map = map;
        this.n = n;
    }

    @Override
    public Long call() {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            try {
                Object obj = pipeline.get();
                assertNotNull(obj, "False awakening");
                assertNotNull(map.remove(obj), "Object is missing " + obj);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return System.nanoTime() - start;
    }
}
