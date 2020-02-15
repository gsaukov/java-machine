package com.apps.authdemo.socketio.model;

public class FilterMessage implements SocketIoMessage {

    private String source;
    private String type;
    private String message;

    public FilterMessage(String source, String type, String message) {
        this.source = source;
        this.type = type;
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
