package com.apps.potok.config;

import com.apps.potok.exchange.notifiers.BalanceNotifier;
import com.apps.potok.exchange.notifiers.ExecutionNotifier;
import com.apps.potok.exchange.notifiers.PositionNotifier;
import com.apps.potok.exchange.notifiers.QuoteNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class ServerTestConfigurator {

    private TaskExecutor executor = new SimpleAsyncTaskExecutor();

    @Autowired
    QuoteNotifier quoteNotifier;

    @Autowired
    PositionNotifier positionNotifier;

    @Autowired
    ExecutionNotifier executionNotifier;

    @Autowired
    BalanceNotifier balanceNotifier;

    public void runServers() {
        runQuoteNotifier();
        runExecutionNotifier();
        runBalanceNotifier();
        runPositionNotifier();
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

}
