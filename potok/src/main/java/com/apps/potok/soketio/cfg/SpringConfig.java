package com.apps.potok.soketio.cfg;

import com.apps.potok.soketio.model.LogFile;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.protocol.Packet;
import com.corundumstudio.socketio.protocol.PacketType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:server.properties")
public class SpringConfig {

    @Bean(name="webSocketServer")
    public SocketIOServer webSocketServer() {

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        final SocketIOServer server = new SocketIOServer(config);
        server.addListeners(new DataListener<LogFile>() {
            @Override
            public void onData(SocketIOClient client, LogFile data, AckRequest ackSender) {

                server.getBroadcastOperations();
            }
        });

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {

                  for(int i=0;i<10;i++){
                      LogFile log = new LogFile();
                      log.setLine("data from server line ["+i+"]");
                      Packet packet = new Packet(PacketType.MESSAGE);
                      packet.setData(log);
                      server.getBroadcastOperations().send(packet);
                  }

            }
        });

        return server;

    }

}
