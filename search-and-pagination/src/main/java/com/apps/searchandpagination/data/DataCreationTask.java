package com.apps.searchandpagination.data;

import com.apps.searchandpagination.data.creators.DataCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataCreationTask implements ApplicationRunner {

    @Autowired
    private DataCreator dataCreator;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dataCreator.createData();
    }
}
