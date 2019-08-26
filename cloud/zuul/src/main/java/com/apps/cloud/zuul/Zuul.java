package com.apps.cloud.zuul;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static org.springframework.boot.WebApplicationType.SERVLET;

@EnableFeignClients
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class Zuul {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Zuul.class).web(SERVLET).run(args);
    }

}
