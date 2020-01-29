package com.apps.authdemo.socketio.model;

public class CsrfMessage {

    private String message;

    public CsrfMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
