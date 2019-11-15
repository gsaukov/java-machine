package com.apps.potok.soketio.config;

import com.apps.potok.soketio.model.LogFile;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
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

    @Value("${socket-io-server.host}")
    private String host;

    @Value("${socket-io-server.port}")
    private Integer port;

    @Value("${certificate.store.key-store}")
    private Resource keystore;

    @Value("${certificate.store.key-store-password}")
    private String keystorePassword;

    @Autowired
    private SeesionAuthorizationListener seesionAuthorizationListener;

    @Autowired
    private SeesionConnectListener seesionConnectListener;

    @Bean(name="webSocketServer")
    public SocketIOServer webSocketServer() throws IOException {

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setKeyStorePassword(keystorePassword);
        config.setKeyStore(keystore.getInputStream());
        config.setAuthorizationListener(seesionAuthorizationListener);

        final SocketIOServer server = new SocketIOServer(config);
        server.addConnectListener(seesionConnectListener);

        return server;

    }

}
