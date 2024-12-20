package com.apps.authdemo.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import org.apache.tomcat.util.net.SecureNioChannel;
import org.springframework.beans.factory.annotation.Autowired;
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
        SecureNioChannel.setSocketIoServer(server);
    }

    @PreDestroy
    public void stop() {
        server.stop();
    }

}
