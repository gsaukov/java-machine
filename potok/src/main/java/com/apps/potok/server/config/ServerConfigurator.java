package com.apps.potok.server.config;

import com.apps.potok.server.eventhandlers.EventNotifierServerV2;
import com.apps.potok.server.exchange.AskContainer;
import com.apps.potok.server.exchange.BidContainer;
import com.apps.potok.server.exchange.OrderCreatorServer;
import com.apps.potok.server.exchange.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.annotation.PreDestroy;

@Configuration
public class ServerConfigurator implements ApplicationListener<ApplicationReadyEvent> {

    private Logger logger = LoggerFactory.getLogger(ServerConfigurator.class);

    @Autowired
    @Qualifier("potokServerRunner")
    private TaskExecutor executor;

    @Bean
    @Qualifier("potokServerRunner")
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Autowired
    private AskContainer askContainer;

    @Autowired
    private BidContainer bidContainer;

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

    @PreDestroy
    public void shutDownHook(){
        orderCreatorServer.stopOrderCreator();
        exchange.stopExchange();
        eventNotifierServer.stopEventNotifier();

        long askLeft = askContainer.size();
        long askInserted = askContainer.getAskInserted();
        long askDecrement = exchange.getAskExecutions();

        long bidLeft = bidContainer.size();
        long bidInserted = bidContainer.getBidInserted();
        long bidDecrement = exchange.getBidExecutions();


        logger.info("AskInit: 10000 AskLeft: " + askLeft + " AskInserted: " + askInserted + " AskDecrement: " + askDecrement + " check: askInit + askInserted - askDecremen = " + (10000 + askInserted - askDecrement) + " must equal to ask left." );
        logger.info("BidInit: 10000 BidLeft: " + bidLeft + " BidInserted: " + bidInserted + " BidDecrement: " + bidDecrement + " check: bidInit + bidInserted - bidDecremen = " + (10000 + bidInserted - bidDecrement) + " must equal to bid left." );
        logger.info("Total check: askInserted + askDecremen + bidInserted + bidDecremen = " + (askInserted + askDecrement + bidInserted + bidDecrement) + " must equal to total Order/MkData Issued");
    }

}
