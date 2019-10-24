package com.apps.potok;

public class AlertStarter {


    public static void main(String[] args){
        AlertServer.RUN_SERVER = true;
        AlertServer server = new AlertServer(new AlertContainer());
        Thread executor = new Thread(server);
        executor.start();
    }

}
