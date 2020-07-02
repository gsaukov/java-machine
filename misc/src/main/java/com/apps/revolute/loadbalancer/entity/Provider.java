package com.apps.revolute.loadbalancer.entity;

import java.net.URI;

public class Provider {

    private final URI address;

    public Provider(URI address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "address=" + address +
                '}';
    }

}
