package com.apps.authdemo;

import org.apache.tomcat.util.net.TLSClientHelloExtractor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthDemoApplication {

    public static TLSClientHelloExtractor tlsClientHelloExtractor;

    public static void main(String[] args) {
        tlsClientHelloExtractor = null;
        SpringApplication.run(AuthDemoApplication.class, args);
    }

}
