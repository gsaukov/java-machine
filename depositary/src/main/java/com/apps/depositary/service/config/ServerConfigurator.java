package com.apps.depositary.service.config;

import com.apps.depositary.service.DepositExecutor;

import com.apps.depositary.service.DepositWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ServerConfigurator implements ApplicationListener<ApplicationReadyEvent> {

    private Logger logger = LoggerFactory.getLogger(ServerConfigurator.class);

    @Autowired
    private DepositExecutor executor;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DepositWorker depositWorker() {
        return new DepositWorker();
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        runDepositExecutor();
    }

    private void runDepositExecutor() {
        executor.runDepositExecutor();
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
        logger.info("Starting depositary shutdown.");
    }

}
