package com.apps.potok.soketio.server;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountContainer {

    private ConcurrentHashMap<String, String> accountContainer = new ConcurrentHashMap<>();

    public void addAccount(String account){
        final String existingAccount = accountContainer.putIfAbsent(account, account);
        if(existingAccount == null){
            // in future if multi session is needed
        } else {
            // in future if multi session is needed
        }
    }

    public boolean containsAccount(String account){
        return accountContainer.contains(account);
    }
}
