package com.apps.finapi.result;

public interface Pipeline {

    void put(Object obj) throws InterruptedException;

    Object get() throws InterruptedException;

}
