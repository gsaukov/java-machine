package com.apps.alertserver;

public class AlertStarter {
    public static void main(String[] args){
        AlertServer.RUN_SERVER = true;
        AlertServer server = new AlertServer();
        Thread executor = new Thread(server);
        executor.start();
    }
}
