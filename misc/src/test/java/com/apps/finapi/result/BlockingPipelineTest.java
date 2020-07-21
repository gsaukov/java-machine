package com.apps.finapi.result;

import org.testng.annotations.Test;

import java.util.Map;
import java.util.concurrent.*;

import static org.testng.Assert.*;

public class BlockingPipelineTest {

    private static final int ITERATIONS = 100000;
    private static final int THREADS = 10;
    private static final long AWAIT = 50l;

    @Test
    public void testPipelineDurability() throws InterruptedException {
        Map<Object, Object> map = new ConcurrentHashMap<>();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADS);
        Pipeline pipeline = new BlockingPipeline();
        for (int i = 1; i <= THREADS / 2; i++) {
            TestProducer producer = new TestProducer(pipeline, map, ITERATIONS);
            TestConsumer consumer = new TestConsumer(pipeline, map, ITERATIONS);
            executor.execute(producer);
            executor.execute(consumer);
        }
        executor.awaitTermination(AWAIT, TimeUnit.SECONDS);

        assertEquals(map.size(), 0, "Objects were lost");
    }

}
