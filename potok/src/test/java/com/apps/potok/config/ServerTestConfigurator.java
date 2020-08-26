package com.apps.potok.config;

import com.apps.potok.exchange.notifiers.BalanceNotifierServer;
import com.apps.potok.exchange.notifiers.ExecutionNotifierServer;
import com.apps.potok.exchange.notifiers.PositionNotifierServer;
import com.apps.potok.exchange.notifiers.QuoteNotifierServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class ServerTestConfigurator {

    private TaskExecutor executor = new SimpleAsyncTaskExecutor();

    @Autowired
    QuoteNotifierServer quoteNotifierServer;

    @Autowired
    PositionNotifierServer positionNotifierServer;

    @Autowired
    ExecutionNotifierServer executionNotifierServer;

    @Autowired
    BalanceNotifierServer balanceNotifierServer;

    public void runServers() {
        runQuoteNotifierServer();
        runExecutionNotifier();
        runBalanceNotifier();
        runPositionNotifier();
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

}
