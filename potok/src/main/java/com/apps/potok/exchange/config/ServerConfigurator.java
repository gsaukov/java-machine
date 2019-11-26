package com.apps.potok.exchange.config;

import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.exchange.eventhandlers.ExecutionNotifierServer;
import com.apps.potok.exchange.eventhandlers.QuoteNotifierServer;
import com.apps.potok.exchange.core.AskContainer;
import com.apps.potok.exchange.core.BidContainer;
import com.apps.potok.exchange.core.OrderCreatorServer;
import com.apps.potok.exchange.core.Exchange;
import com.apps.potok.exchange.init.Initiator;
import com.apps.potok.exchange.mkdata.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${exchange.order-size}")
    private Integer orderSize;

    @Autowired
    private Initiator initiator;

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
    private OrderManager orderManager;

    @Autowired
    private QuoteNotifierServer quoteNotifierServer;

    @Autowired
    private ExecutionNotifierServer executionNotifierServer;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        initiator.initiateContainer(orderSize, askContainer.get(), Route.BUY);
        initiator.initiateContainer(orderSize, bidContainer.get(), Route.SELL);
        runQuoteNotifierServer();
        runExchange();
        runOrderCreatorServer();
        runExecutionNotifier();
    }

    public void runExchange() {
        executor.execute(exchange);
    }

    public void runOrderCreatorServer() {
        executor.execute(orderCreatorServer);
    }

    public void runQuoteNotifierServer() {
        executor.execute(quoteNotifierServer);
    }

    public void runExecutionNotifier() {
        executor.execute(executionNotifierServer);
    }

    @PreDestroy
    public void shutDownHook(){
        orderCreatorServer.stopOrderCreator();
        exchange.stopExchange();
        quoteNotifierServer.stopQuoteNotifier();
        executionNotifierServer.stopQuoteNotifier();

        long askInit = initiator.getAskInit();
        long askLeft = askContainer.size();
        long askInserted = askContainer.getAskInserted();
        long askDecrement = exchange.getAskExecutions() + orderManager.getCancelled(Route.BUY);

        long bidInit = initiator.getBidInit();
        long bidLeft = bidContainer.size();
        long bidInserted = bidContainer.getBidInserted();
        long bidDecrement = exchange.getBidExecutions() + orderManager.getCancelled(Route.SELL);;


        logger.info("AskInit: " + askInit + " AskLeft: " + askLeft + " AskInserted: " + askInserted + " AskDecrement: " + askDecrement + " check: askInit + askInserted - askDecremen = " + (askInit + askInserted - askDecrement) + " must equal to ask left." );
        logger.info("BidInit: " + bidInit + " BidLeft: " + bidLeft + " BidInserted: " + bidInserted + " BidDecrement: " + bidDecrement + " check: bidInit + bidInserted - bidDecremen = " + (bidInit + bidInserted - bidDecrement) + " must equal to bid left." );
        logger.info("Total check: askInserted + askDecremen + bidInserted + bidDecremen = " + (askInserted + askDecrement + bidInserted + bidDecrement) + " must equal to total Order/MkData Issued");
    }

}
