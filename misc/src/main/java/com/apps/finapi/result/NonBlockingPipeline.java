package com.apps.finapi.result;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A container which serves as a pipeline for a single element. The pipeline makes sure that no elements get lost,
 * i.e. you can only put a new element into the pipeline when the previous element has first been retrieved from it.
 */
public class NonBlockingPipeline implements Pipeline {

    private final AtomicReference<Object> reference = new AtomicReference<>();

    /**
     * Puts an element into the pipeline. If there is already an element in the pipeline, this method blocks and waits
     * until that element has been removed and the new element could be put into the pipeline.
     *
     * @param obj the element to put into the pipeline.
     */
    public void put(Object obj) {
        while (!reference.compareAndSet(null, obj));
    }

    /**
     * Removes and returns the current element from the pipeline. If there is no element in the pipeline, this method
     * blocks and waits until there is an element.
     *
     * @return the element that has been in the pipeline and that has now been removed from it.
     */
    public Object get() {
        Object obj = null;
        boolean exchanged = false;
        while (!exchanged) {
            obj = reference.get();
            if(obj != null) {
                exchanged = reference.compareAndSet(obj, null);
            }
        };
        return obj;
    }

}
