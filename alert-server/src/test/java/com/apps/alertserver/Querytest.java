package com.apps.alertserver;

import org.testng.annotations.Test;

public class Querytest {

    @Test
    public void queryTest(){
        AlertServer server = new AlertServer();
        server.printSymbols();
        server.searchOffers(server.getSymbols().get(5), 60);
    }

}
