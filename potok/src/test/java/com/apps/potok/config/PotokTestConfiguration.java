package com.apps.potok.config;

import com.apps.potok.exchange.config.ServerConfigurator;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.exchange.eventhandlers.BalanceNotifierServer;
import com.apps.potok.exchange.eventhandlers.ExecutionNotifierServer;
import com.apps.potok.exchange.eventhandlers.PositionNotifierServer;
import com.apps.potok.exchange.eventhandlers.QuoteNotifierServer;
import com.apps.potok.exchange.eventhandlers.QuoteSubscribersV2;
import com.apps.potok.exchange.init.Initiator;
import com.apps.potok.exchange.mkdata.Route;
import com.apps.potok.soketio.config.SpringConfig;
import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOServer;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;


import javax.annotation.PostConstruct;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ComponentScan(basePackages = { "com.apps.potok.*" },
        excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.apps.potok.soketio.*"),
                          @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.apps.potok.websocket.*")})
public class PotokTestConfiguration {

    @MockBean
    ServerConfigurator serverConfigurator;

    @MockBean
    SocketIOServer server;

    @MockBean
    SpringConfig springConfig;

    @MockBean
    QuoteSubscribersV2 quoteSubscriber;

    @Autowired
    private ServerTestConfigurator serverTestConfigurator;

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