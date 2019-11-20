package com.apps.potok.soketio.server;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
public class AccountContainer {

    private ConcurrentHashMap<String, ConcurrentLinkedDeque<UUID>> accountContainer = new ConcurrentHashMap<>();

    public void addAccount(String account, UUID client){
        ConcurrentLinkedDeque<UUID> newAccount = new ConcurrentLinkedDeque();
        ConcurrentLinkedDeque<UUID> existingAccount = accountContainer.putIfAbsent(account, newAccount);
        if(existingAccount == null){
            newAccount.offer(client);
        } else {
            existingAccount.offer(client);
        }
    }

    public boolean containsAccount(String account){
        return accountContainer.containsKey(account);
    }

    public ConcurrentLinkedDeque<UUID> getAccountClients(String account){
        return accountContainer.get(account);
    }

    public ConcurrentLinkedDeque<UUID> removeAccountClient(String account, UUID client){
        ConcurrentLinkedDeque<UUID> clients = accountContainer.get(account);
        if(clients != null && !clients.isEmpty()){
            clients.remove(client);
        }
        return clients;
    }
}
