package com.apps.cloud.justitia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class Justitia {

    public static void main(String[] args) {
        SpringApplication.run(Justitia.class, args);
    }

}