package com.apps.cloud.zuul;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

import javax.net.ssl.HttpsURLConnection;

import static org.springframework.boot.WebApplicationType.SERVLET;

@EnableFeignClients
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class Zuul {

    public static void main(String[] args) {
        //TODO used to support custom certificates by feign clients. Disable certificate origin host validation. Only for dev.
        HttpsURLConnection.setDefaultHostnameVerifier ((hostname, session) -> true);

        new SpringApplicationBuilder(Zuul.class).web(SERVLET).run(args);
    }

}
