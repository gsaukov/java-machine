package com.apps.authdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AuthDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthDemoApplication.class, args);
    }

}
