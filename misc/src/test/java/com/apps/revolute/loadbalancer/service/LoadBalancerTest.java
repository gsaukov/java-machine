package com.apps.revolute.loadbalancer.service;

import com.apps.revolute.loadbalancer.entity.Provider;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class LoadBalancerTest {

    public static final Integer TEST_SEED = 10;

    @Test(expectedExceptions = IllegalStateException.class)
    public void registerMaxProviders(){
        LoadBalancer balancer = new LoadBalancer(TEST_SEED);
        for (int i = 0; i < 11; i++){
            balancer.addProvider(getRandomUri());
        }
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void registerSameProviders(){
        LoadBalancer balancer = new LoadBalancer(TEST_SEED);
        Provider provider = getRandomUri();
        balancer.addProvider(provider);
        balancer.addProvider(provider);
    }

    @Test
    public void register10ProvidersSuccess(){
        LoadBalancer balancer = new LoadBalancer(TEST_SEED);
        for (int i = 0; i < 10; i++){
            balancer.addProvider(getRandomUri());
        }
    }

    @Test
    public void getProviderSuccess(){
        List<Provider> expectedProviders = new ArrayList<>();
        LoadBalancer balancer = new LoadBalancer(TEST_SEED);
        for (int i = 0; i < 5; i++){
            balancer.addProvider(getUri(i));
        }
        for (int i = 0; i < 5; i++){
            expectedProviders.add(balancer.get());
        }
        System.out.println(Arrays.toString(expectedProviders.toArray()));
    }

    private Provider getRandomUri(){
        UUID uuid = UUID.randomUUID();
        URI uri = URI.create("http://" + uuid.toString());
        return new Provider(uri);
    }

    private Provider getUri(Integer num){
        URI uri = URI.create(num.toString());
        return new Provider(uri);
    }

}
