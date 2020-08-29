package com.apps.potok.config;

import com.apps.potok.exchange.config.ServerConfigurator;
import com.apps.potok.exchange.init.ShutDowner;
import com.apps.potok.exchange.randombehavior.AccountServerExecutor;
import com.apps.potok.soketio.listeners.QuoteSubscribersV2;
import com.apps.potok.kafka.consumer.DepositMessageConsumer;
import com.apps.potok.kafka.producer.ExecutionMessageProducer;
import com.apps.potok.soketio.config.SpringConfig;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOServer;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;


import javax.annotation.PostConstruct;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ComponentScan(basePackages = { "com.apps.potok.*" },
        excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.apps.potok.soketio.*"),
                          @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.apps.potok.websocket.*"),
                          @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.apps.potok.kafka.*")})
public class PotokTestConfiguration {

    @MockBean
    ServerConfigurator serverConfigurator;

    @MockBean
    AccountServerExecutor accountServerExecutor;

    @MockBean
    SocketIOServer server;

    @MockBean
    SpringConfig springConfig;

    @MockBean
    QuoteSubscribersV2 quoteSubscriber;

    @Autowired
    private ServerTestConfigurator serverTestConfigurator;

    @MockBean
    DepositMessageConsumer depositMessageConsumer;

    @MockBean
    ExecutionMessageProducer executionMessageProducer;

    @MockBean
    ShutDowner shutDowner;

    @PostConstruct
    public void runAllServers () {
        prepareSocketIo();
        serverTestConfigurator.runServers();
    }

    private void prepareSocketIo() {
        BroadcastOperations broadcastOperations = Mockito.mock(BroadcastOperations.class);
        when(server.getRoomOperations(any(String.class))).thenReturn(broadcastOperations);
    }

}
