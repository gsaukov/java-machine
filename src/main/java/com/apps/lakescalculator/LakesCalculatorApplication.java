package com.apps.lakescalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("lakescalculator")
public class LakesCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LakesCalculatorApplication.class, args);
    }

}
