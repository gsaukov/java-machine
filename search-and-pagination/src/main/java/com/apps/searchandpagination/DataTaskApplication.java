package com.apps.searchandpagination;

import com.apps.searchandpagination.data.DataCreationTaskConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
@EnableTask
public class DataTaskApplication {
    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(DataTaskApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
        System.exit(SpringApplication.exit(context));
    }

    @Bean
    public DataCreationTaskConfigurer getTaskConfigurer(){
        return new DataCreationTaskConfigurer(dataSource);
    }
}
