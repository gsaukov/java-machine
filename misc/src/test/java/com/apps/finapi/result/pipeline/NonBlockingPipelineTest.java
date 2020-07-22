package com.apps.finapi.result.pipeline;

import com.apps.finapi.result.NonBlockingPipeline;
import com.apps.finapi.result.Pipeline;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class NonBlockingPipelineTest {

    private static final int ITERATIONS = 100000;
    private static final int THREADS = 40;

    @BeforeTest
    public void warmUpJWM(){
//        https://stackoverflow.com/questions/504103/how-do-i-write-a-correct-micro-benchmark-in-java
        for(int i = 0; i<3; i++){
            Pipeline pipeline = new NonBlockingPipeline();
            TestPipelineService service = new TestPipelineService(pipeline, THREADS, ITERATIONS);
            service.performPipelineDurabilityTesting();
            System.out.println("NonBlockingPipeline warmup average execution time of " + THREADS + " threads " + ITERATIONS
                    + " iterations is " + service.getAverageExecTime() + " ns.");
        }
    }


    @Test
    public void testPipelineDurability() {
        Pipeline pipeline = new NonBlockingPipeline();
        TestPipelineService service = new TestPipelineService(pipeline, THREADS, ITERATIONS);
        service.performPipelineDurabilityTesting();
        System.out.println("NonBlockingPipeline benchmark average execution time of " + THREADS + " threads " + ITERATIONS + " iterations is "
                + service.getAverageExecTime() + " ns.");
        assertEquals(service.getResults().size(), 0, "Objects were lost");
    }

}
