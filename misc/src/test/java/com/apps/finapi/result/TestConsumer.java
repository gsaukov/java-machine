package com.apps.finapi.result;

import java.util.Map;

import static org.testng.Assert.assertNotNull;

public class TestConsumer extends Thread {

    private final Pipeline pipeline;
    private final Map map;
    private final int n;

    public TestConsumer(Pipeline pipeline, Map map, int n) {
        this.pipeline = pipeline;
        this.map = map;
        this.n = n;
    }

    @Override
    public void run() {
        for (int i = 0; i < n; i++) {
            try {
                Object obj = pipeline.get();
                assertNotNull(obj, "False awakening");
                assertNotNull(map.remove(obj), "Object is missing " + obj);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
