package com.apps.potok.exchange.config;

import com.apps.potok.exchange.account.BalanceCalculator;
import com.apps.potok.exchange.core.Exchange;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.exchange.notifiers.BalanceNotifierServer;
import com.apps.potok.exchange.notifiers.ExecutionNotifierServer;
import com.apps.potok.exchange.notifiers.PositionNotifierServer;
import com.apps.potok.exchange.notifiers.QuoteNotifierServer;
import com.apps.potok.exchange.core.AskContainer;
import com.apps.potok.exchange.core.BidContainer;
import com.apps.potok.exchange.randombehavior.AccountServerExecutor;
import com.apps.potok.exchange.init.Initiator;
import com.apps.potok.exchange.randombehavior.MkMakerServer;
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

@Configuration
public class ServerConfigurator implements ApplicationListener<ApplicationReadyEvent> {

    private Logger logger = LoggerFactory.getLogger(ServerConfigurator.class);

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
    private MkMakerServer mkDataServer;

    @Autowired
    private AccountServerExecutor accountServerExecutor;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private QuoteNotifierServer quoteNotifierServer;

    @Autowired
    private ExecutionNotifierServer executionNotifierServer;

    @Autowired
    private BalanceNotifierServer balanceNotifierServer;

    @Autowired
    private PositionNotifierServer positionNotifierServer;

    @Autowired
    private BalanceCalculator balanceCalculator;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        initiator.initiate();
        runQuoteNotifierServer();
        runMkDataServer();
        runAccountServers();
        runExecutionNotifier();
        runBalanceNotifier();
        runPositionNotifier();
        addGraceShutdown();
    }

    public void runMkDataServer() {
        executor.execute(mkDataServer);
    }

    public void runAccountServers() {
        accountServerExecutor.runAccountServers();
    }

    public void runQuoteNotifierServer() {
        executor.execute(quoteNotifierServer);
    }

    public void runExecutionNotifier() {
        executor.execute(executionNotifierServer);
    }

    public void runBalanceNotifier() {
        executor.execute(balanceNotifierServer);
    }

    private void runPositionNotifier() {
        executor.execute(positionNotifierServer);
    }

    private void addGraceShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutDownHook();
            }
        });
    }

    public void shutDownHook(){
        logger.info("Starting potok shutdown.");
        accountServerExecutor.stopAccountServers();
        mkDataServer.stopExchangeServer();
        quoteNotifierServer.stopExchangeServer();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executionNotifierServer.stopExchangeServer();
        balanceNotifierServer.stopExchangeServer();
        positionNotifierServer.stopExchangeServer();

        balanceCalculator.calculateBalance();
    }

}
