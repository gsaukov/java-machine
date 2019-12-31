package com.apps.potok;

import com.apps.potok.config.BaseTest;
import com.apps.potok.config.ExchangeCondition;
import com.apps.potok.config.TestScenario;
import com.apps.potok.config.TestScenarioCreator;
import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.BalanceCalculator;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.Position;
import com.apps.potok.exchange.core.Route;
import com.apps.potok.soketio.model.execution.Execution;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class AccountExecutionsTest extends BaseTest {

    @Autowired
    private TestScenarioCreator testScenarioCreator;

    @Autowired
    private BalanceCalculator balanceCalculator;

    private TestScenario testScenario;
    private final int BALANCE = 100000;

    @Test
    public void fullFillPosition() throws InterruptedException {
        String symbol = "SYMBOL_" + RandomStringUtils.randomAlphabetic(10).toUpperCase();
        int BUY_PRICE = 8;
        int volume = 25;
        testScenario = testScenarioCreator.newTestScenario(symbol, BALANCE, exchangeConditions(symbol));
        Order order = testScenarioCreator.sendNewOrder(testScenario, Route.BUY, BUY_PRICE, volume);
        Thread.sleep(10);

        assertEquals(order.getVolume().intValue(), 0); // fully filled

        //  6 * 10 = 60
        //  7 * 10 = 70
        //  8 * 5  = 40
        //      25 | 170

        assertEquals(testScenario.getBalance(), BALANCE - 170);

        Position position = testScenario.getAccount().getPosition(symbol);
        assertEquals(position.getVolume().intValue(), volume);

        List<Map.Entry<Integer, AtomicInteger>> aggregations = new ArrayList(position.getBuyPriceValueAggregation().entrySet());
        assertEquals(aggregations.size(), 3);
        assertEquals(aggregations.get(0).getKey().intValue(), 6);
        assertEquals(aggregations.get(0).getValue().intValue(), 10);
        assertEquals(aggregations.get(1).getKey().intValue(), 7);
        assertEquals(aggregations.get(1).getValue().intValue(), 10);
        assertEquals(aggregations.get(2).getKey().intValue(), 8);
        assertEquals(aggregations.get(2).getValue().intValue(), 5);

        ArrayList<Execution> executions = new ArrayList(position.getBuyExecutions().values());
        executions.sort(new ExecutionsComparator());

        assertEquals(executions.get(0).getFillPrice().intValue(), 6);
        assertEquals(executions.get(0).getQuantity().intValue(), 10);
        assertEquals(executions.get(0).getOrderUuid(), order.getUuid());
        assertEquals(executions.get(0).getAccountId(), testScenario.getAccountId());
        assertEquals(executions.get(1).getFillPrice().intValue(), 7);
        assertEquals(executions.get(1).getQuantity().intValue(), 10);
        assertEquals(executions.get(1).getOrderUuid(), order.getUuid());
        assertEquals(executions.get(1).getAccountId(), testScenario.getAccountId());
        assertEquals(executions.get(2).getFillPrice().intValue(), 8);
        assertEquals(executions.get(2).getQuantity().intValue(), 5);
        assertEquals(executions.get(2).getOrderUuid(), order.getUuid());
        assertEquals(executions.get(2).getAccountId(), testScenario.getAccountId());

        // put new order sell 10@7 on exchange;
        // new exchange conditions.
        // | ASK BUY    | BID SELL   |
        // | price qty  | price qty  |
        // | 5     10   | 7     10   |
        // | 4     10   | 8     5   |
        // | 3     10   | 9     10   |
        // | 2     10   |

        testScenarioCreator.prepareExchange(new ExchangeCondition(testScenarioCreator.createAccount(100000),
                symbol, 1, 0, 7, 10));

        order = testScenarioCreator.sendNewOrder(testScenario, Route.BUY, 8, 10);
        Thread.sleep(10);
        assertEquals(testScenario.getBalance(), BALANCE - 170 - 70);

        aggregations = new ArrayList(position.getBuyPriceValueAggregation().entrySet());
        assertEquals(aggregations.get(1).getKey().intValue(), 7);
        assertEquals(aggregations.get(1).getValue().intValue(), 20);

        executions = new ArrayList(position.getBuyExecutions().values());
        executions.sort(new ExecutionsComparator());

        assertEquals(executions.size(), 4);
        assertEquals(executions.get(2).getFillPrice().intValue(), 7);
        assertEquals(executions.get(2).getQuantity().intValue(), 10);
        assertEquals(executions.get(2).getOrderUuid(), order.getUuid());
        assertEquals(executions.get(2).getAccountId(), testScenario.getAccountId());

        order = testScenarioCreator.sendNewOrder(testScenario, Route.SELL, 3, 25);
        Thread.sleep(10);
        assertEquals(order.getVolume().intValue(), 0); // fully filled

        //  5 * 10 = 50
        //  4 * 10 = 40
        //  3 * 5  = 15
        //      25 | 105

        assertEquals(testScenario.getBalance(), BALANCE - 170 - 70 + 105);

        //only 10 should remain
        assertEquals(position.getVolume().intValue(), 10);

        List<Map.Entry<Integer, AtomicInteger>> sellAggregations = new ArrayList(position.getSellPriceValueAggregation().entrySet());
        assertEquals(sellAggregations.size(), 3);
        assertEquals(sellAggregations.get(0).getKey().intValue(), 3);
        assertEquals(sellAggregations.get(0).getValue().intValue(), 5);
        assertEquals(sellAggregations.get(1).getKey().intValue(), 4);
        assertEquals(sellAggregations.get(1).getValue().intValue(), 10);
        assertEquals(sellAggregations.get(2).getKey().intValue(), 5);
        assertEquals(sellAggregations.get(2).getValue().intValue(), 10);

        ArrayList<Execution> sellExecutions = new ArrayList(position.getSellExecutions().values());
        sellExecutions.sort(new ExecutionsComparator());

        assertEquals(sellExecutions.get(0).getFillPrice().intValue(), 3);
        assertEquals(sellExecutions.get(0).getQuantity().intValue(), 5);
        assertEquals(sellExecutions.get(0).getOrderUuid(), order.getUuid());
        assertEquals(sellExecutions.get(0).getAccountId(), testScenario.getAccountId());
        assertEquals(sellExecutions.get(1).getFillPrice().intValue(), 4);
        assertEquals(sellExecutions.get(1).getQuantity().intValue(), 10);
        assertEquals(sellExecutions.get(1).getOrderUuid(), order.getUuid());
        assertEquals(sellExecutions.get(1).getAccountId(), testScenario.getAccountId());
        assertEquals(sellExecutions.get(2).getFillPrice().intValue(), 5);
        assertEquals(sellExecutions.get(2).getQuantity().intValue(), 10);
        assertEquals(sellExecutions.get(2).getOrderUuid(), order.getUuid());
        assertEquals(sellExecutions.get(2).getAccountId(), testScenario.getAccountId());

        order = testScenarioCreator.sendNewOrder(testScenario, Route.SHORT, 2, 10);
        Thread.sleep(10);
        assertEquals(order.getVolume().intValue(), 0); // fully filled

        //  2 * 5 = 10
        //  3 * 5 = 15
        //     10 | 25

        assertEquals(testScenario.getBalance(), BALANCE - 170 - 70 + 105 - (order.getBlockedPrice() * order.getOriginalVolume()) + 25);

        Position shortPosition = testScenario.getAccount().getShortPosition(symbol);
        assertEquals(shortPosition.getVolume().intValue(), -10);

        List<Map.Entry<Integer, AtomicInteger>> shortAggregations = new ArrayList(shortPosition.getSellPriceValueAggregation().entrySet());
        assertEquals(shortAggregations.size(), 2);
        assertEquals(shortAggregations.get(0).getKey().intValue(), 2);
        assertEquals(shortAggregations.get(0).getValue().intValue(), 5);
        assertEquals(shortAggregations.get(1).getKey().intValue(), 3);
        assertEquals(shortAggregations.get(1).getValue().intValue(), 5);

        ArrayList<Execution> shortExecutions = new ArrayList(shortPosition.getSellExecutions().values());
        shortExecutions.sort(new ExecutionsComparator());

        assertEquals(shortExecutions.get(0).getFillPrice().intValue(), 2);
        assertEquals(shortExecutions.get(0).getQuantity().intValue(), 5);
        assertEquals(shortExecutions.get(0).getOrderUuid(), order.getUuid());
        assertEquals(shortExecutions.get(0).getAccountId(), testScenario.getAccountId());
        assertEquals(shortExecutions.get(1).getFillPrice().intValue(), 3);
        assertEquals(shortExecutions.get(1).getQuantity().intValue(), 5);
        assertEquals(shortExecutions.get(1).getOrderUuid(), order.getUuid());
        assertEquals(shortExecutions.get(1).getAccountId(), testScenario.getAccountId());

        testScenarioCreator.manageCloseShort(testScenario, symbol, 5);
        assertEquals(testScenario.getBalance(), BALANCE - 170 - 70 + 105 - (order.getBlockedPrice() * order.getOriginalVolume()) + 25 + (order.getBlockedPrice() * 5));

        Thread.sleep(100);
        Map<String, Long> deviations = balanceCalculator.calculateBalance();
        assertNull(deviations.get(testScenario.getAccount().getAccountId()));
    }

    @Test
    public void shortBalanceCalculation() throws InterruptedException {
        String symbol = "SYMBOL_" + RandomStringUtils.randomAlphabetic(10).toUpperCase();
        testScenario = testScenarioCreator.newTestScenario(symbol, BALANCE, exchangeConditions(symbol));

        Order order = testScenarioCreator.sendNewOrder(testScenario, Route.SHORT, 5, 10);
        Thread.sleep(300);
        order = testScenarioCreator.sendNewOrder(testScenario, Route.SHORT, 5, 10);
        Thread.sleep(300);
        balanceCalculator.calculateBalance();

    }

    // prepares new exchange conditions.
    // | ASK BUY    | BID SELL   |
    // | price qty  | price qty  |
    // | 5     10   | 6     10   |
    // | 4     10   | 7     10   |
    // | 3     10   | 8     10   |
    // | 2     10   | 9     10   |

    private List<ExchangeCondition> exchangeConditions(String symbol){
        Account exchangeAccount = testScenarioCreator.createAccount(100000);
        List<ExchangeCondition> exchangeConditions = new ArrayList<>();
        exchangeConditions.add(new ExchangeCondition(exchangeAccount, symbol, 4, 5, 6, 10));
        return exchangeConditions;
    }

    static class ExecutionsComparator implements Comparator<Execution> {

        @Override
        public int compare(Execution o1, Execution o2) {
            if(o1.getFillPrice().equals(o2.getFillPrice())){
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            } else {
                return o1.getFillPrice().compareTo(o2.getFillPrice());
            }
        }
    }


}
