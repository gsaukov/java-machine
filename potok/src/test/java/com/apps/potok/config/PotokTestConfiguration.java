package com.apps.potok.config;

import com.apps.potok.exchange.config.ServerConfigurator;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.exchange.eventhandlers.BalanceNotifierServer;
import com.apps.potok.exchange.eventhandlers.ExecutionNotifierServer;
import com.apps.potok.exchange.eventhandlers.PositionNotifierServer;
import com.apps.potok.exchange.eventhandlers.QuoteNotifierServer;
import com.apps.potok.exchange.eventhandlers.QuoteSubscribersV2;
import com.apps.potok.exchange.init.Initiator;
import com.apps.potok.soketio.config.SpringConfig;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(basePackages = { "com.apps.potok.*" },
        excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.apps.potok.soketio.*"),
                          @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.apps.potok.websocket.*")})
public class PotokTestConfiguration {

    @MockBean
    ServerConfigurator serverConfigurator;

    @MockBean
    QuoteNotifierServer quoteNotifierServer;

    @MockBean
    PositionNotifierServer positionNotifierServer;

    @MockBean
    ExecutionNotifierServer executionNotifierServer;

    @MockBean
    BalanceNotifierServer balanceNotifierServer;

    @MockBean
    QuoteSubscribersV2 quoteSubscriber;

    @MockBean
    SpringConfig springConfig;

}