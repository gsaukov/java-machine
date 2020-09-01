package com.apps.potok.exchange.config;

import com.apps.potok.exchange.account.BalanceCalculator;
import com.apps.potok.exchange.core.Exchange;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.exchange.notifiers.BalanceNotifier;
import com.apps.potok.exchange.notifiers.ExecutionNotifier;
import com.apps.potok.exchange.notifiers.PositionNotifier;
import com.apps.potok.exchange.notifiers.QuoteNotifier;
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
    private MkMakerServer mkMakerServer;

    @Autowired
    private AccountServerExecutor accountServerExecutor;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private QuoteNotifier quoteNotifier;

    @Autowired
    private ExecutionNotifier executionNotifier;

    @Autowired
    private BalanceNotifier balanceNotifier;

    @Autowired
    private PositionNotifier positionNotifier;

    @Autowired
    private BalanceCalculator balanceCalculator;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        initiator.initiate();
        runQuoteNotifier();
        runMkDataServer();
        runAccountServers();
        runExecutionNotifier();
        runBalanceNotifier();
        runPositionNotifier();
        addGraceShutdown();
    }

    public void runMkDataServer() {
        executor.execute(mkMakerServer);
    }

    public void runAccountServers() {
        accountServerExecutor.runAccountServers();
    }

    public void runQuoteNotifier() {
        executor.execute(quoteNotifier);
    }

    public void runExecutionNotifier() {
        executor.execute(executionNotifier);
    }

    public void runBalanceNotifier() {
        executor.execute(balanceNotifier);
    }

    private void runPositionNotifier() {
        executor.execute(positionNotifier);
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
        mkMakerServer.stopExchangeServer();
        quoteNotifier.stopExchangeServer();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executionNotifier.stopExchangeServer();
        balanceNotifier.stopExchangeServer();
        positionNotifier.stopExchangeServer();

        balanceCalculator.calculateBalance();
    }

}
