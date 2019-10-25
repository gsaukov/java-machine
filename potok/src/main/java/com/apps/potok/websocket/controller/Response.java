package com.apps.potok.websocket.controller;

public class Response {

    private String name;

    public Response() {
    }

    public Response(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
