package com.apps.potok.soketio.server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Account {

    private final String accountId;
    private final AtomicLong balance;
    private final ConcurrentHashMap<UUID, UUID> clientUuids;

    public Account(String accountId, long balance) {
        this.accountId = accountId;
        this.balance = new AtomicLong(balance);
        this.clientUuids = new ConcurrentHashMap<UUID, UUID>();
    }

    public void addClientUuid(UUID client){
        clientUuids.putIfAbsent(client, client);
    }

    public List<UUID> getClientUuids(){
        return new ArrayList<>(clientUuids.keySet());
    }

    public List<UUID> removeClient(UUID client) {
        if(clientUuids != null && !clientUuids.isEmpty()){
            clientUuids.remove(client);
        }
        return getClientUuids();
    }

    public String getAccountId() {
        return accountId;
    }

    public long getBalance() {
        return balance.get();
    }

    //could be optimised with LongUnaryOperator
    public boolean doNegativeOrderBalance(long risk, long change) {
        long prev, next;
        do {
            prev = balance.get();
            if(prev - risk > 0){
                return false;
            }
            next = prev - change;
        } while (!balance.compareAndSet(prev, next));
        return true;
    }

    public boolean doPositiveOrderBalance(long change) {
        balance.getAndAdd(change);
        return true;
    }
}
