package com.apps.searchandpagination.data;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataCreationTask implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.currentTimeMillis();
    }
}
