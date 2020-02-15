package com.apps.authdemo.socketio;

import com.apps.authdemo.socketio.model.SetSourceRequest;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import org.springframework.stereotype.Service;

@Service
public class SetSourceListener implements DataListener<SetSourceRequest> {

    public static String SOURCE = "BASIC";

    @Override
    public void onData(SocketIOClient client, SetSourceRequest data, AckRequest ackRequest) {
        SOURCE = data.getRequest();
    }

}
