package com.apps.authdemo.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import org.apache.tomcat.util.net.SecureNioChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class Bootstrap {

    @Autowired
    private SocketIOServer server;

    @PostConstruct
    public void start() {
        server.start();
        SecureNioChannel.setSocketIoProxy(socketIoProxy());
    }

    @PreDestroy
    public void stop() {
        server.stop();
    }

    @Bean
    private SocketIoProxy socketIoProxy() {
        return new SocketIoProxy(server);
    }

}
