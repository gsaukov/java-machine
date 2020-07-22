package com.apps.finapi.result.pipeline;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * This test checks durability of Pipeline thread safe implementations Blocking and NonBlocking and prints out
 * benchmarks in to console. The test reveals that functionally both implementations are correct however non blocking
 * is ~20 times faster especially after JVM warm up.
 * Some hints: https://stackoverflow.com/questions/504103/how-do-i-write-a-correct-micro-benchmark-in-java
 * Number of Pipeline.get() calls must be equal to Pipeline.put() otherwise it will not leave while loop or await block.
 */
public class PipelineTest {

    private static final int ITERATIONS = 100000;
    private static final int THREADS = 40;

    @Test
    public void testNonBlockingDurabilityAndBench() {
        Pipeline pipeline = new NonBlockingPipeline();
        TestPipelineService service = new TestPipelineService(pipeline, THREADS, ITERATIONS);
        service.performPipelineDurabilityTesting();
        printBench("NonBlockingPipeline", service.getAverageExecTimeMs());
        assertEquals(service.getResults().size(), 0, "Objects were lost");
    }

    @Test
    public void testBlockingDurabilityAndBench() {
        Pipeline pipeline = new BlockingPipeline();
        TestPipelineService service = new TestPipelineService(pipeline, THREADS, ITERATIONS);
        service.performPipelineDurabilityTesting();
        printBench("BlockingPipeline", service.getAverageExecTimeMs());
        assertEquals(service.getResults().size(), 0, "Objects were lost");
    }

    private void printBench(String name, Long averageExecTimeMs) {
        System.out.println(name + " average execution time of " + THREADS + " threads " + ITERATIONS + " iterations is "
                + averageExecTimeMs + " ms.");
    }
}
