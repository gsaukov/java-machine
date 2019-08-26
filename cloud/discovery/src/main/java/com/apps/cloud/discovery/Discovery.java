package com.apps.cloud.discovery;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import static org.springframework.boot.WebApplicationType.SERVLET;

@EnableEurekaServer
@SpringBootApplication
public class Discovery {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Discovery.class).web(SERVLET).run(args);
    }

}
