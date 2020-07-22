package com.apps.finapi.result.pipeline;

public interface Pipeline {

    void put(Object obj) throws InterruptedException;

    Object get() throws InterruptedException;

}
