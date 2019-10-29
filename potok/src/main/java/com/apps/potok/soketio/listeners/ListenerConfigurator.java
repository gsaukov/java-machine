package com.apps.potok.soketio.listeners;

import com.apps.potok.soketio.model.LogFile;
import com.apps.potok.soketio.model.quote.QuoteRequest;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class ListenerConfigurator {

    @Autowired
    private SocketIOServer server;

    @Autowired
    private ChatMessageListener chatMessageListener;

    @Autowired
    private QuoteMessageListener quoteMessageListener;

    @PostConstruct
    public void webSocketServer() throws IOException {
        server.addEventListener("message", LogFile.class, chatMessageListener);
        server.addEventListener("quoteRequest", QuoteRequest.class, quoteMessageListener);
    }
}
