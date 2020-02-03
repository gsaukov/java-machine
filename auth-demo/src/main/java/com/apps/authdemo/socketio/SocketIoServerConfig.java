package com.apps.authdemo.socketio;

import com.apps.authdemo.socketio.model.EnableTlsMessagingRequest;
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
public class SocketIoServerConfig {

    @Value("${socket-io-server.host}")
    private String host;

    @Value("${socket-io-server.port}")
    private Integer port;

    @Value("${certificate.store.key-store}")
    private Resource keystore;

    @Value("${certificate.store.key-store-password}")
    private String keystorePassword;


    @Autowired
    private SessionConnectListener sessionConnectListener;

    @Autowired
    private SessionDisconnectListener sessionDisconnectListener;

    @Autowired
    private EnableTlsMessagingListener enableTlsMessagingListener;

    @Bean(name="webSocketServer")
    public SocketIOServer webSocketServer() throws IOException {

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setKeyStorePassword(keystorePassword);
        config.setKeyStore(keystore.getInputStream());

        SocketIOServer server = new SocketIOServer(config);
        server.addConnectListener(sessionConnectListener);
        server.addDisconnectListener(sessionDisconnectListener);
        server.addEventListener("enableTlsMessagingRequest", EnableTlsMessagingRequest.class, enableTlsMessagingListener);
        return server;

    }

}
