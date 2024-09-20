package com.apps.potok.exchange.account;

import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.Position;
import com.apps.potok.soketio.model.execution.Accountable;
import com.apps.potok.soketio.model.execution.CloseShortPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.apps.potok.exchange.core.Route.SELL;
import static com.apps.potok.exchange.core.Route.SHORT;

public class Account {
    //todo to resolve issue with balance modification/ position on sell orders due to multiple clients account should have order queue.
    //todo orders should be replaced with following structure: ConcurrentHashMap<String, ConcurrentHashMap<UUID, Order>> for performance.
    private final String accountId;
    private final Long initializedBalance;
    private final AtomicLong balance;
    private final ConcurrentHashMap<UUID, Order> orders;
    private final ConcurrentHashMap<String, Position> positions;
    private final ConcurrentHashMap<String, Position> shortPositions;
    private final ConcurrentHashMap<UUID, UUID> clientUuids;

    public Account(String accountId, long balance) {
        this.accountId = accountId;
        this.initializedBalance = balance;
        this.balance = new AtomicLong(balance);
        this.orders = new ConcurrentHashMap();
        this.clientUuids = new ConcurrentHashMap();
        this.positions = new ConcurrentHashMap();
        this.shortPositions = new ConcurrentHashMap();
    }

    public void addClientUuid(UUID client){
        clientUuids.putIfAbsent(client, client);
    }

    public List<UUID> getClientUuids(){
        return new ArrayList<>(clientUuids.keySet());
    }

    public List<UUID> removeClient(UUID client) {
        if(!clientUuids.isEmpty()){
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
    //risk is only to make sure we have some money(10%) on account, only change is applied to the balance.
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

    public boolean doCloseShortPosition(CloseShortPosition closeShortPosition) {
        Position positivePosition = getPosition(closeShortPosition.getSymbol());
        Position shortPosition = getShortPosition(closeShortPosition.getSymbol());
        long sellOrderSize = getExistingSellOrderVolume(closeShortPosition.getSymbol());
        if(canCloseShort(closeShortPosition, positivePosition, shortPosition, sellOrderSize)){
            shortPosition.closeShort(closeShortPosition, positivePosition);
            balance.getAndAdd(shortPosition.getBlockedPrice() * closeShortPosition.getAmount());
        }
        return true;
    }

    private boolean canCloseShort(CloseShortPosition closeShortPosition, Position positivePosition, Position shortPosition, long sellOrderSize) {
        if(positivePosition == null){
            return false;
        }
        if(shortPosition == null){
            return false;
        }
        if((positivePosition.getVolume() - sellOrderSize) < closeShortPosition.getAmount()){
            return false;
        }
        if(Math.abs(shortPosition.getVolume()) < closeShortPosition.getAmount()){
            return false;
        }
        return true;
    }


    public Position getPosition(String symbol){
        return positions.get(symbol);
    }

    public Position getShortPosition(String symbol){
        return shortPositions.get(symbol);
    }

    public Collection<Position> getPositions(){
        return positions.values();
    }

    public Collection<Position> getShortPositions(){
        return shortPositions.values();
    }

    public long getExistingSellOrderVolume(String symbol) {
        long res = 0l;
        for(Order order : getOrders()){
            if(SELL.equals(order.getRoute()) && order.getSymbol().equals(symbol) && order.isActive()){
                res += order.getVolume();
            }
        }
        return res;
    }

    public long getExistingPositivePositionVolume(String symbol) {
        long res = 0l;
        Position position = getPosition(symbol);
        if(position != null && position.getVolume() > 0){
            res = position.getVolume();
        }
        return res;
    }

    public Position doExecution(Accountable execution) {
        if (SHORT.equals(execution.getRoute())) {
            return doShortExecution(execution);
        } else {
            return doBuySellExecution(execution);
        }
    }

    private Position doBuySellExecution (Accountable execution) {
        Position newPosition = new Position(execution);
        Position existingPosition = positions.putIfAbsent(execution.getSymbol(), newPosition);
        if(existingPosition != null) {
            existingPosition.applyExecution(execution);
            return existingPosition;
        } else {
            return newPosition;
        }
    }

    private Position doShortExecution(Accountable execution) {
        Position newPosition = new Position(execution);
        Position existingPosition = shortPositions.putIfAbsent(execution.getSymbol(), newPosition);
        if(existingPosition != null) {
            existingPosition.applyExecution(execution);
            return existingPosition;
        } else {
            return newPosition;
        }
    }

    public void addOrder(Order order){
        orders.put(order.getUuid(), order);
    }

    public Collection<Order> getOrders(){
        return orders.values();
    }

    public Order getOrder(UUID uuid){
        return orders.get(uuid);
    }

    public Long getInitializedBalance() {
        return initializedBalance;
    }
}
