package com.apps.searchandpagination;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@EnableEurekaClient
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.apps.searchandpagination.*" },
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "com.apps.searchandpagination.data.*"))
public class SearchDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchDataApplication.class, args);
    }

}
