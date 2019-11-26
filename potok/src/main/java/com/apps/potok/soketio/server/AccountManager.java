package com.apps.potok.soketio.server;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountManager {

    public final static String MK_MAKER = "MK_MAKER";
    public final static String TEST_ACCOUNT_ID = "TEST_ACCOUNT_ID";

    @Value("${exchange.accounts-size}")
    private Integer accountsSize;

    private final ConcurrentHashMap<String, Account> accountContainer;
    private final List<String> accountIds = new ArrayList<>();

    public AccountManager() {
        this.accountContainer = new ConcurrentHashMap<>();
    }

    @PostConstruct
    private void postConstruct (){
        for(int i = 0 ; i < accountsSize ; i++){
            String accountId = RandomStringUtils.randomAlphabetic(9).toUpperCase();
            Account account = new Account(accountId, RandomUtils.nextLong(100000l, 10000000l));
            accountContainer.put(accountId, account);
        }
        accountIds.addAll(accountContainer.keySet());
        Collections.unmodifiableList(accountIds);
        Account mkMaker = new Account(MK_MAKER, 9999999999l);
        accountContainer.put(MK_MAKER, mkMaker);
        Account testAccount = new Account(TEST_ACCOUNT_ID, 9999999l);
        accountContainer.put(TEST_ACCOUNT_ID, testAccount);
    }

    public List<String> getAllAccountIds() { //for random exchange activity only.
        return accountIds;
    }

    public Account addAccount(String accountId, UUID client){
        Account account = accountContainer.get(accountId);
        if (account != null) {
            account.addClientUuid(client);
        }
        return account;
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

    public Account getAccount(String account) {
        return accountContainer.get(account);
    }
}
