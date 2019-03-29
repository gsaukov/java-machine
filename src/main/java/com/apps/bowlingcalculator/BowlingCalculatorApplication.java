package com.apps.bowlingcalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("bowlingcalculator")
public class BowlingCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(BowlingCalculatorApplication.class, args);
    }

}
