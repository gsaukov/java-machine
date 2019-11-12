package com.apps.potok.server.config;

import com.apps.potok.server.eventhandlers.EventNotifierServerV2;
import com.apps.potok.server.exchange.OrderCreatorServer;
import com.apps.potok.server.exchange.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class ServerConfigurator implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    @Qualifier("potokServerRunner")
    private TaskExecutor executor;

    @Bean
    @Qualifier("potokServerRunner")
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Autowired
    private Exchange exchange;

    @Autowired
    private OrderCreatorServer orderCreatorServer;

    @Autowired
    private EventNotifierServerV2 eventNotifierServer;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        runEventNotifierServer();
        runExchange();
        runOrderCreatorServer();
    }

    public void runExchange() {
        executor.execute(exchange);
    }

    public void runOrderCreatorServer() {
        executor.execute(orderCreatorServer);
    }

    public void runEventNotifierServer() {
        executor.execute(eventNotifierServer);
    }

}
