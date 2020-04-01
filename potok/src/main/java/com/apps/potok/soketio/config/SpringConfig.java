package com.apps.potok.soketio.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
@PropertySource("classpath:server.properties")
public class SpringConfig {

    @Value("${socket-io-server.port}")
    private Integer port;

    @Value("${certificate.store.key-store}")
    private Resource keystore;

    @Value("${certificate.store.key-store-password}")
    private String keystorePassword;

    @Autowired
    private SessionAuthorizationListener sessionAuthorizationListener;

    @Autowired
    private SessionConnectListener sessionConnectListener;

    @Autowired
    private SessionDisconnectListener sessionDisconnectListener;

    @Bean(name="webSocketServer")
    public SocketIOServer webSocketServer() throws IOException {

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setPort(port);
        config.setKeyStorePassword(keystorePassword);
        config.setKeyStore(keystore.getInputStream());
        config.setAuthorizationListener(sessionAuthorizationListener);

        SocketIOServer server = new SocketIOServer(config);
        server.addConnectListener(sessionConnectListener);
        server.addDisconnectListener(sessionDisconnectListener);

        return server;

    }

}
