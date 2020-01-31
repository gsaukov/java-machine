package com.apps.authdemo.socketio.model;

public class BasicAuthMessage implements SocketIoMessage {

    private String message;

    public BasicAuthMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
