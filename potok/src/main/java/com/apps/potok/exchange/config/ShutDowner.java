package com.apps.potok.exchange.config;

import com.apps.potok.exchange.core.AbstractExchangeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service //Shutdown timeout.
public class ShutDowner extends AbstractExchangeServer {

    @Autowired
    private ServerConfigurator serverConfigurator;

    @PostConstruct
    public void postConstruct() {
        serverConfigurator.taskExecutor().execute(this);
    }

    @Override
    public void runExchangeServer() {
        try {
            Thread.sleep(600000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverConfigurator.shutDownHook();
    }

    @Override
    public void speedControl() {
    }
}
