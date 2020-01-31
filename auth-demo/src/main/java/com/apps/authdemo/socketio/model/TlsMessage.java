package com.apps.authdemo.socketio.model;

public class TlsMessage implements SocketIoMessage {

    private String message;

    public TlsMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
