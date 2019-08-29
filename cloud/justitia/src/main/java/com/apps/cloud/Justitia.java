package com.apps.cloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import static org.springframework.boot.WebApplicationType.SERVLET;

@EnableEurekaClient
@SpringBootApplication
public class Justitia {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Justitia.class).web(SERVLET).run(args);
    }

}