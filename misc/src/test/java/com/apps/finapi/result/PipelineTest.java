package com.apps.finapi.result;

import org.testng.annotations.Test;

import java.util.Map;
import java.util.concurrent.*;

import static org.testng.Assert.*;

public class PipelineTest {

    private static final int ITERATIONS = 10000;
    private static final int THREADS = 10;
    private static final long AWAIT = 5l;

    @Test
    public void testPipelineDurability() throws InterruptedException {
        Map<Object, Object> map = new ConcurrentHashMap<>();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADS);
        Pipeline pipeline = new Pipeline();
        for (int i = 1; i <= THREADS / 2; i++) {
            TestProducer producer = new TestProducer(pipeline, map, ITERATIONS);
            TestConsumer consumer = new TestConsumer(pipeline, map, ITERATIONS);
            executor.execute(producer);
            executor.execute(consumer);
        }
        executor.awaitTermination(AWAIT, TimeUnit.SECONDS);

        assertEquals(map.size(), 0, "Objects were lost");
    }

    private static class TestProducer extends Thread {

        private final Pipeline pipeline;
        private final Map<Object, Object> map;
        private final int n;

        public TestProducer(Pipeline pipeline, Map<Object, Object> map, int n) {
            this.pipeline = pipeline;
            this.map = map;
            this.n = n;
        }

        @Override
        public void run() {
            for (int i = 0; i < n; i++) {
                try {
                    Object obj = new Object();
                    map.put(obj, obj);
                    pipeline.put(obj);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class TestConsumer extends Thread {

        private final Pipeline pipeline;
        private final Map<Object, Object> map;
        private final int n;

        public TestConsumer(Pipeline pipeline, Map<Object, Object> map, int n) {
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

}
