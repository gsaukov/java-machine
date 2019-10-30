package com.apps.potok.server.config;

import com.apps.potok.server.exchange.OrderCreatorServer;
import com.apps.potok.server.exchange.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.PostConstruct;

@Configuration
public class ServerConfigurator {

    @Autowired
    @Qualifier("potokServerRunner")
    private TaskExecutor executor;

    @Autowired
    private Exchange alertServer;

    @Autowired
    private OrderCreatorServer alertCreatorServer;

    @Bean
    @Qualifier("potokServerRunner")
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @PostConstruct
    public void runAlertServer() {
        executor.execute(alertServer);
    }

    @PostConstruct
    public void runAlertCreatorServer() {
        executor.execute(alertCreatorServer);
    }

}
