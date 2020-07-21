package com.apps.finapi.result.pipeline;

import com.apps.finapi.result.BlockingPipeline;
import com.apps.finapi.result.Pipeline;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BlockingPipelineTest {

    private static final int ITERATIONS = 100000;
    private static final int THREADS = 40;

    @Test
    public void testPipelineDurability() {
        Pipeline pipeline = new BlockingPipeline();
        PipelineTestService service = new PipelineTestService(pipeline, THREADS, ITERATIONS);
        service.performPipelineDurabilityTesting();
        System.out.println("BlockingPipeline average execution time of " + THREADS + " threads " + ITERATIONS + " iterations is "
                + service.getAverageExecTime() + " ms.");
        assertEquals(service.getResults().size(), 0, "Objects were lost");
    }

}
