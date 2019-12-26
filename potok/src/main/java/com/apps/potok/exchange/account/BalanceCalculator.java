package com.apps.potok.exchange.account;

import com.apps.potok.exchange.config.ServerConfigurator;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.Position;
import com.apps.potok.soketio.model.execution.Accountable;
import com.apps.potok.soketio.model.execution.CloseShortPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.apps.potok.exchange.core.Route.BUY;
import static com.apps.potok.exchange.core.Route.SHORT;

@Service
public class BalanceCalculator {

    private Logger logger = LoggerFactory.getLogger(ServerConfigurator.class);

    @Autowired
    private AccountManager accountManager;

    public void calculateBalance() {
        logger.info("Starting Balance calculation");
        for(Account account : accountManager.getAllAccounts()) {
            calculateAccountBalance(account);
        }
        logger.info("Balance calculation complete");
    }

    private void calculateAccountBalance(Account account) {
        long initializedBalance = account.getInitializedBalance();
        long finalBalance = account.getBalance();
        long ordersNegativeBalance =  calculateOrdersBalance(account.getOrders());
        long executionsShortBalance =  calculateExecutionsShortBalance(account.getShortPositions());
        long executionsBalance =  calculateExecutionsBalance(account.getPositions());
        long actualDiff = initializedBalance - finalBalance;
        long expectedDiff = executionsShortBalance + executionsBalance - ordersNegativeBalance;
        if(actualDiff + expectedDiff != 0){
            logger.info("Balance mismatch initializedBalance " + initializedBalance + " finalBalance " + finalBalance + " ordersNegativeBalance " +
                    ordersNegativeBalance + " executionsShortBalance " + executionsShortBalance + " executionsBalance " + executionsBalance + " actualDiff " +
                    actualDiff  + " expectedDiff " + expectedDiff);
        }
    }

    private long calculateExecutionsBalance(Collection<Position> positions) {
        long executionsBalance = 0l;
        for(Position position : positions){
            executionsBalance = executionsBalance + calculateExecutionBalance(position);
        }
        return executionsBalance;
    }

    private long calculateExecutionBalance(Position position) {
        long executionBuyBalance = 0l;
        long executionSellBalance = 0l;

        for(Accountable execution : position.getBuyExecutions().values()) {
            if(!execution.isDeposit()){
                executionBuyBalance = executionBuyBalance + (execution.getQuantity() * execution.getFillPrice());
            }
        }

        for(Accountable execution : position.getSellExecutions().values()) {
            if(!execution.isDeposit()){
                executionSellBalance = executionSellBalance + (execution.getQuantity() * execution.getFillPrice());
            }
        }

        return executionSellBalance - executionBuyBalance;
    }

    private long calculateExecutionsShortBalance(Collection<Position> positions) {
        long executionsShortBalance = 0l;
        for(Position position : positions){
            executionsShortBalance = executionsShortBalance + calculateExecutionShortBalance(position);
        }
        return executionsShortBalance;
    }

    private long calculateExecutionShortBalance(Position position) {
        long executionShortBalance = 0l;
        long executionQuantity = 0l;
        long closedShortQuantity = 0l;
        for(CloseShortPosition closeShortPosition : position.getCloseShort().values()) {
            closedShortQuantity = closedShortQuantity + closeShortPosition.getAmount();
        }
        for(Accountable execution : position.getSellExecutions().values()) {
            if(!execution.isDeposit()){
                executionQuantity = executionQuantity + execution.getQuantity();
            }
        }
        executionShortBalance = (closedShortQuantity - executionQuantity) * position.getBlockedPrice();
        return executionShortBalance;
    }

    private long calculateOrdersBalance(Collection<Order> orders) {
        long ordersBalance = 0l;
        for(Order order : orders){
            ordersBalance = ordersBalance + calculateOrderBalance(order);
        }
        return ordersBalance;
    }

    private long calculateOrderBalance(Order order) {
        long orderBalance = 0l;
        if(order.isActive()){
            if(BUY.equals(order.getRoute())){
                orderBalance = order.getVolume() * order.getVal();
            } else if (SHORT.equals(order.getRoute())){
                orderBalance = order.getOriginalVolume() * order.getBlockedPrice();
            }
        }
        return orderBalance;
    }
}
