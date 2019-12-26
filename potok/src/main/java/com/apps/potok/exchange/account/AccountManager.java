package com.apps.potok.exchange.account;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountManager {

    public final static String MK_MAKER = "MK_MAKER";
    public final static String TEST_ACCOUNT_ID = "TEST_ACCOUNT_ID";

    private final ConcurrentHashMap<String, Account> accountContainer;
    private final ArrayList<String> allAccountIds;

    public AccountManager() {
        this.accountContainer = new ConcurrentHashMap<>();
        allAccountIds = new ArrayList<>();
    }

    public List<String> getAllAccountIds() { //for random exchange activity only, initialization use only.
        return allAccountIds;
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accountContainer.values());
    }

    public Account addClient(String accountId, UUID client){
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

    public void addNewAccount(Account account){
        accountContainer.put(account.getAccountId(), account);
        if(!MK_MAKER.equals(account.getAccountId()) && !TEST_ACCOUNT_ID.equals(account.getAccountId())){
            allAccountIds.add(account.getAccountId());
        }
    }
}
