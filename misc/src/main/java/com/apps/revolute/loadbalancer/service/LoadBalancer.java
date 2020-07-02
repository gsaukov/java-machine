package com.apps.revolute.loadbalancer.service;

import com.apps.revolute.loadbalancer.entity.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoadBalancer {

    public static final int MAX_PROVIDERS = 10;

    private final List<Provider> providers = new ArrayList<>();
    private final Random r;

    public LoadBalancer(int seed) {
        this.r = new Random(seed);
    }

    public void addProvider(Provider provider) throws IllegalStateException {
        if(providers.size() >= MAX_PROVIDERS){
            throw new IllegalStateException("The max number of accepted providers from the load balancer is 10");
        }

        if(providers.contains(provider)){
            throw new IllegalStateException("Provider is already registered");
        }

        providers.add(provider);
    }

    public Provider get() {
        return providers.get(r.nextInt(providers.size()));
    }

}
