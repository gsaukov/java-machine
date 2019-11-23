package com.apps.potok.soketio.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AccountManager {

    @Autowired
    private AccountContainer accountContainer;

    public Account getAccount(String account){
        return accountContainer.getAccount(account);
    }

    public void addAccount(String account, UUID client){
        accountContainer.addAccount(account, client);
    }

    public boolean containsAccount(String account){
        return accountContainer.containsAccount(account);
    }

    public List<UUID> getAccountClients(String account){
        return accountContainer.getAccountClients(account);
    }

    public List<UUID> removeAccountClient(String account, UUID client){
       return accountContainer.removeAccountClient(account, client);
    }
}
