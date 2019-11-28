package com.apps.potok.soketio.server;

import com.apps.potok.exchange.core.Position;
import com.apps.potok.soketio.model.execution.Execution;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Account {

    private final String accountId;
    private final AtomicLong balance;
    private final ConcurrentHashMap<String, Position> positions;
    private final ConcurrentHashMap<UUID, UUID> clientUuids;

    public Account(String accountId, long balance) {
        this.accountId = accountId;
        this.balance = new AtomicLong(balance);
        this.clientUuids = new ConcurrentHashMap();
        this.positions = new ConcurrentHashMap();
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
            next = prev - change;
            if(prev - risk < 0 || next < 0){
                return false;
            }
        } while (!balance.compareAndSet(prev, next));
        return true;
    }

    public boolean doPositiveOrderBalance(long change) {
        balance.getAndAdd(change);
        return true;
    }

    public Position doExecution(Execution execution) {
        Position newPosition = new Position(execution);
        Position existingPosition = positions.putIfAbsent(execution.getSymbol(), newPosition);
        if(existingPosition != null) {
            existingPosition.applyExecution(execution);
            return existingPosition;
        } else {
            return newPosition;
        }
    }

}
