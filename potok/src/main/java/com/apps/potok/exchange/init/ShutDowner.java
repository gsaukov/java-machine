package com.apps.potok.exchange.init;

import com.apps.potok.exchange.config.ServerConfigurator;
import com.apps.potok.exchange.core.AbstractExchangeServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service //Shutdown timeout.
public class ShutDowner extends AbstractExchangeServer {

    @Value("${exchange.shutdowner.timeout}")
    private Integer timeout;

    @Autowired
    private ServerConfigurator serverConfigurator;

    @PostConstruct
    public void postConstruct() {
        if(timeout > 0){
            serverConfigurator.taskExecutor().execute(this);
        }
    }

    @Override
    public void runExchangeServer() {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverConfigurator.shutDownHook();
    }

    @Override
    public void speedControl() {
    }
}
