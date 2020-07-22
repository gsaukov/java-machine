package com.apps.finapi.result.pipeline;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A container which serves as a pipeline for a single element. The pipeline makes sure that no elements get lost,
 * i.e. you can only put a new element into the pipeline when the previous element has first been retrieved from it.
 */
public class BlockingPipeline implements Pipeline {

    /**
     * classic https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/locks/Condition.html
     * use PipelineTest to test it.
     */
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private Object element;

    /**
     * Puts an element into the pipeline. If there is already an element in the pipeline, this method blocks and waits
     * until that element has been removed and the new element could be put into the pipeline.
     *
     * @param obj the element to put into the pipeline.
     */
    @Override
    public void put(Object obj) throws InterruptedException {
        lock.lock();
        try {
            while (element != null)
                notFull.await();
            element = obj;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes and returns the current element from the pipeline. If there is no element in the pipeline, this method
     * blocks and waits until there is an element.
     *
     * @return the element that has been in the pipeline and that has now been removed from it.
     */
    @Override
    public Object get() throws InterruptedException {
        lock.lock();
        try {
            while (element == null)
                notEmpty.await();
            Object obj = element;
            element = null;
            notFull.signal();
            return obj;
        } finally {
            lock.unlock();
        }
    }

}
