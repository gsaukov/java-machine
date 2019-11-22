package com.apps.potok.soketio.server;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountContainer {

    public final static String MK_MAKER = "MK_MAKER";

    @Value("${exchange.accounts-size}")
    private Integer accountsSize;

    private final ConcurrentHashMap<String, Account> accountContainer;

    public AccountContainer() {
        this.accountContainer = new ConcurrentHashMap<>();
    }

    @PostConstruct
    private void postConstruct (){
        for(int i = 0 ; i < accountsSize ; i++){
            String accountId = RandomStringUtils.randomAlphabetic(9).toUpperCase();
            Account account = new Account(accountId, RandomUtils.nextLong(100000l, 10000000l));
            accountContainer.put(accountId, account);
        }
        Account mkMaker = new Account(MK_MAKER, 9999999999l);
        accountContainer.put(MK_MAKER, mkMaker);
    }

    public List<String> getAllAccountIds() {
        return new ArrayList<>(accountContainer.keySet());
    }

    public void addAccount(String accountId, UUID client){
        Account account = accountContainer.get(accountId);
        if (account != null) {
            account.addClientUuid(client);
        }
    }

    public boolean containsAccount(String account){
        return accountContainer.containsKey(account);
    }

    public List<UUID> getAccountClients(String account){
        return accountContainer.get(account).getClientUuids();
    }

    public List<UUID> removeAccountClient(String account, UUID client){
        return accountContainer.get(account).removeClient(client);
    }
}
