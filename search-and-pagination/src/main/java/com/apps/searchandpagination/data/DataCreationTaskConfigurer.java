package com.apps.searchandpagination.data;

import org.springframework.cloud.task.configuration.DefaultTaskConfigurer;

import javax.sql.DataSource;

public class DataCreationTaskConfigurer extends DefaultTaskConfigurer {

    public DataCreationTaskConfigurer(DataSource dataSource) {
        super(dataSource);
    }
}
