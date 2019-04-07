package com.apps.searchandpagination;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@Profile("searchandpagination")
public class SearchDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchDataApplication.class, args);
    }

}
