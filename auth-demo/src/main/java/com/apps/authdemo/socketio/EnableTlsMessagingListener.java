package com.apps.authdemo.socketio;

import com.apps.authdemo.socketio.model.EnableTlsMessagingRequest;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import org.apache.tomcat.util.net.SecureNioChannel;
import org.springframework.stereotype.Service;

@Service
public class EnableTlsMessagingListener implements DataListener<EnableTlsMessagingRequest> {

    @Override
    public void onData(SocketIOClient client, EnableTlsMessagingRequest data, AckRequest ackRequest) {
        SecureNioChannel.setSocketIoReady(true);
    }

}
