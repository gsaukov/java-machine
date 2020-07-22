package com.apps.finapi.result.pipeline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class TestPipelineService {

    private final Pipeline pipeline;
    private final int threads;
    private final int iterations;
    private final Map results;
    private Long averageExecTime;

    public TestPipelineService(Pipeline pipeline, int threads, int iterations) {
        this.pipeline = pipeline;
        this.threads = threads;
        this.iterations = iterations;
        this.results = new ConcurrentHashMap();
    }

    public void performPipelineDurabilityTesting() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
        List<Future<Long>> futures = initiateExecutor(executor);
        averageExecTime = awaitCompletion(futures);
        executor.shutdown();
    }

    private List<Future<Long>> initiateExecutor(ThreadPoolExecutor executor) {
        List<Future<Long>> futures = new ArrayList<>();
        for (int i = 1; i <= threads / 2; i++) {
            Callable producer = new TestPipelineProducer(pipeline, results, iterations);
            Callable consumer = new TestPipelineConsumer(pipeline, results, iterations);
            futures.add(executor.submit(producer));
            futures.add(executor.submit(consumer));
        }
        return futures;
    }

    private Long awaitCompletion(List<Future<Long>> futures) {
        Long execTime = 0l;
        for (Future<Long> future : futures) {
            try {
                execTime += future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return execTime / threads;
    }

    public Map getResults() {
        return results;
    }

    public Long getAverageExecTimeNs() {
        return averageExecTime;
    }

    public Long getAverageExecTimeMs() {
        return averageExecTime/1000000;
    }
}
