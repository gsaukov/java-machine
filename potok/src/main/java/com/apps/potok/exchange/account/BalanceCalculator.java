package com.apps.potok.exchange.account;

import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.Position;
import com.apps.potok.soketio.model.execution.Accountable;
import com.apps.potok.soketio.model.execution.CloseShortPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.apps.potok.exchange.core.Route.BUY;
import static com.apps.potok.exchange.core.Route.SHORT;

@Service
public class BalanceCalculator {

    private Logger logger = LoggerFactory.getLogger(BalanceCalculator.class);

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private BalanceCalculationStatistics statistics;

    public Map<String, Long> calculateBalance() {
        logger.info("Starting Balance calculation");
        Map<String, Long> deviations = new HashMap<>();
        statistics.addAccounts(accountManager.getAllAccounts().size());
        for(Account account : accountManager.getAllAccounts()) {
            long deviation = calculateAccountBalance(account);
            if(deviation != 0){
                deviations.put(account.getAccountId(), deviation);
            }
        }
        logger.info("Balance calculation complete");
        statistics.printStatistics();
        return deviations;
    }

    private long calculateAccountBalance(Account account) {
        long initializedBalance = account.getInitializedBalance();
        long finalBalance = account.getBalance();
        long ordersNegativeBalance =  calculateOrdersBalance(account.getOrders());
        long executionsShortBalance =  calculateExecutionsShortBalance(account.getShortPositions());
        long executionsBalance =  calculateExecutionsBalance(account.getPositions());
        long actualDiff = initializedBalance - finalBalance;
        long expectedDiff = executionsShortBalance + executionsBalance - ordersNegativeBalance;
        long deviation = actualDiff + expectedDiff;
        if(deviation != 0){
            logger.info("Balance mismatch accountId " + account.getAccountId() + " initializedBalance " + initializedBalance +
                    " finalBalance " + finalBalance + " ordersNegativeBalance " + ordersNegativeBalance + " executionsShortBalance "
                    + executionsShortBalance + " executionsBalance " + executionsBalance + " actualDiff " + actualDiff  +
                    " expectedDiff " + expectedDiff);
        }
        return deviation;
    }

    private long calculateExecutionsBalance(Collection<Position> positions) {
        long executionsBalance = 0l;
        statistics.addPositions(positions.size());
        for(Position position : positions){
            executionsBalance = executionsBalance + calculateExecutionBalance(position);
        }
        return executionsBalance;
    }

    private long calculateExecutionBalance(Position position) {
        long executionBuyBalance = 0l;
        long executionSellBalance = 0l;
        statistics.addExecutions(position.getBuyExecutions().size());
        for(Accountable execution : position.getBuyExecutions().values()) {
            if(!execution.isDeposit()){
                executionBuyBalance = executionBuyBalance + (execution.getQuantity() * execution.getFillPrice());
            }
        }
        statistics.addExecutions(position.getSellExecutions().size());
        for(Accountable execution : position.getSellExecutions().values()) {
            if(!execution.isDeposit()){
                executionSellBalance = executionSellBalance + (execution.getQuantity() * execution.getFillPrice());
            }
        }

        return executionSellBalance - executionBuyBalance;
    }

    private long calculateExecutionsShortBalance(Collection<Position> positions) {
        long executionsShortBalance = 0l;
        statistics.addShortPositions(positions.size());
        for(Position position : positions){
            executionsShortBalance = executionsShortBalance + calculateExecutionShortBalance(position);
        }
        return executionsShortBalance;
    }

    private long calculateExecutionShortBalance(Position position) {
        long executionShortBalance = 0l;
        long executionQuantity = 0l;
        long closedShortQuantity = 0l;
        long executionSellBalance = 0l;
        statistics.addCloseShorts(position.getCloseShort().size());
        for(CloseShortPosition closeShortPosition : position.getCloseShort().values()) {
            closedShortQuantity = closedShortQuantity + closeShortPosition.getAmount();
        }
        statistics.addExecutions(position.getSellExecutions().size());
        for(Accountable execution : position.getSellExecutions().values()) {
            if(!execution.isDeposit()){
                executionQuantity = executionQuantity + execution.getQuantity(); //negative for account is taken from his balance
                executionSellBalance = executionSellBalance + Math.abs((execution.getQuantity() * execution.getFillPrice()));
            }
        }
        executionShortBalance = (closedShortQuantity - executionQuantity) * position.getBlockedPrice();
        return executionShortBalance + executionSellBalance;
    }

    private long calculateOrdersBalance(Collection<Order> orders) {
        statistics.addOrders(orders.size());
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
                orderBalance = order.getVolume() * order.getBlockedPrice();
            }
        }
        return orderBalance;
    }
}
