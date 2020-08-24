package com.apps.potok.exchange.core;

import com.apps.potok.soketio.model.execution.Accountable;
import com.apps.potok.soketio.model.execution.CloseShortPosition;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.apps.potok.exchange.core.Route.BUY;

//Non blocking position
public class Position {

    private final UUID uuid;
    private final Date createdTimestamp;
    private final String symbol;
    private final String accountId;
    private final AtomicInteger volume;
    private final Route route;
    private final Integer blockedPrice;
    private final ConcurrentHashMap<Integer, AtomicInteger> buyPriceValueAggregation; //this will be empty for short position
    private final ConcurrentHashMap<UUID, Accountable> buyExecutions; //this will be empty for short position
    private final ConcurrentHashMap<Integer, AtomicInteger> sellPriceValueAggregation;
    private final ConcurrentHashMap<UUID, Accountable> sellExecutions;
    private final ConcurrentHashMap<UUID, CloseShortPosition> closeShort;

    public Position(Accountable accountable) {
        if(accountable.isDeposit()){
            this.uuid = accountable.getUuid();
        } else {
            this.uuid = UUID.randomUUID();
        }
        this.createdTimestamp = accountable.getTimestamp();
        this.symbol = accountable.getSymbol();
        this.accountId = accountable.getAccountId();
        this.route = accountable.getRoute();
        this.buyPriceValueAggregation = new ConcurrentHashMap<>();
        this.buyExecutions = new ConcurrentHashMap<>();
        this.sellPriceValueAggregation = new ConcurrentHashMap<>();
        this.sellExecutions = new ConcurrentHashMap<>();
        this.closeShort = new ConcurrentHashMap<>();
        this.blockedPrice = accountable.getBlockedPrice();
        this.volume = new AtomicInteger(0);
        applyExecution(accountable);
    }

    public void applyExecution (Accountable execution) {
        if(!buyExecutions.containsKey(execution.getUuid()) && !sellExecutions.containsKey(execution.getUuid())) {
            if(BUY.equals(execution.getRoute())) {
                applyBuyExecution(execution);
            } else {
                applySellShortExecution(execution);
            }
        }
    }

    private void applyBuyExecution (Accountable execution) {
        AtomicInteger newVolume = new AtomicInteger(execution.getQuantity());
        AtomicInteger existingVolume = buyPriceValueAggregation.putIfAbsent(execution.getFillPrice(), newVolume);
        if(existingVolume != null) {
            existingVolume.addAndGet(newVolume.get());
        }
        buyExecutions.put(execution.getUuid(), execution);
        volume.getAndAdd(execution.getQuantity());
    }


    private void applySellShortExecution(Accountable execution) {
        AtomicInteger newVolume = new AtomicInteger(execution.getQuantity());
        AtomicInteger existingVolume = sellPriceValueAggregation.putIfAbsent(execution.getFillPrice(), newVolume);
        if(existingVolume != null) {
            existingVolume.addAndGet(newVolume.get());
        }
        sellExecutions.put(execution.getUuid(), execution);
        volume.getAndAdd(-execution.getQuantity());
    }

    //this method is always invoked on short position
    public void closeShort(CloseShortPosition closeShortPosition, Position positivePosition){
        this.closeShort.put(closeShortPosition.getUuid(), closeShortPosition);
        positivePosition.closeShort.put(closeShortPosition.getUuid(), closeShortPosition);
        this.volume.getAndAdd(closeShortPosition.getAmount());
        positivePosition.volume.getAndAdd(-closeShortPosition.getAmount());
        closeShortPosition.setPositivePosition(positivePosition.getUuid());
        closeShortPosition.setShortPosition(this.uuid);
    }

    public Double calculateWeightedAveragePrice() {
        double weight = 0d, volume = 0d;
        for(Map.Entry<Integer, AtomicInteger> entry : buyPriceValueAggregation.entrySet()) {
            long tierPrice = entry.getKey();
            long tierVolume = entry.getValue().get();
            weight += tierPrice * tierVolume;
            volume += tierVolume;
        }
        if(volume == 0){
            return 0d;
        } else {
            return weight/volume;
        }
    }

    public Double calculateAveragePerformance() {
        //todo think, implement
        return 0d;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getAccountId() {
        return accountId;
    }

    public Route getRoute() {
        return route;
    }

    public Integer getBlockedPrice() {
        return blockedPrice;
    }

    public Integer getVolume() {
        return volume.get();
    }

    public ConcurrentHashMap<UUID, Accountable> getBuyExecutions() {
        return buyExecutions;
    }

    public ConcurrentHashMap<UUID, Accountable> getSellExecutions() {
        return sellExecutions;
    }

    public ConcurrentHashMap<Integer, AtomicInteger> getBuyPriceValueAggregation() {
        return buyPriceValueAggregation;
    }

    public ConcurrentHashMap<Integer, AtomicInteger> getSellPriceValueAggregation() {
        return sellPriceValueAggregation;
    }

    public ConcurrentHashMap<UUID, CloseShortPosition> getCloseShort() {
        return closeShort;
    }

}
