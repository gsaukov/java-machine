package com.apps.potok.config;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.exchange.core.*;
import com.apps.potok.soketio.model.execution.CloseShortPosition;
import com.apps.potok.soketio.model.execution.CloseShortPositionRequest;
import com.apps.potok.soketio.model.order.NewOrder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListMap;

@TestComponent
public class TestScenarioCreator {

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private CloseShortManager closeShortManager;

    @Autowired
    private SymbolContainer symbolContainer;

    @Autowired
    private AskContainer askContainer;

    @Autowired
    private BidContainer bidContainer;

    public TestScenario newTestScenario() {
        String symbol = "SYMBOL_" + RandomStringUtils.randomAlphabetic(10).toUpperCase();
        return newTestScenario(symbol, 100000);
    }

    public TestScenario newTestScenario(String symbol, int balance) {
        TestScenario testScenario = new TestScenario();
        testScenario.setSymbol(symbol);
        Account account = createAccount(balance);
        testScenario.setAccount(account);
        Account exchangeAccount = createAccount(100000);
        ExchangeCondition exchangeCondition = new ExchangeCondition(exchangeAccount, symbol, 4, 5, 6, 1);
        prepareExchange(exchangeCondition);
        testScenario.addExchangeCondition(exchangeCondition);
        return testScenario;
    }

    public TestScenario newTestScenario(String symbol, int balance, List<ExchangeCondition> exchangeConditions) {
        TestScenario testScenario = new TestScenario();
        testScenario.setSymbol(symbol);
        Account account = createAccount(balance);
        testScenario.setAccount(account);
        for(ExchangeCondition exchangeCondition :exchangeConditions){
            prepareExchange(exchangeCondition);
        }
        testScenario.addExchangeConditions(exchangeConditions);
        return testScenario;
    }

    // prepares new exchange conditions.
    // | ASK BUY   | BID SELL  |
    // | price qty | price qty |
    // | 5     1   | 6     1   |
    // | 4     1   | 7     1   |
    // | 3     1   | 8     1   |
    // | 2     1   | 9     1   |


    public void prepareExchange(ExchangeCondition exchangeCondition) {
        symbolContainer.addSymbol(exchangeCondition.getSymbol(), Math.max(exchangeCondition.getAskPrice(), exchangeCondition.getBidPrice()));

        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> askSymbolOrderContainer = askContainer.get(exchangeCondition.getSymbol());
        if(askSymbolOrderContainer == null){
            askSymbolOrderContainer = new ConcurrentSkipListMap(new AskComparator());
            askContainer.put(exchangeCondition.getSymbol(), askSymbolOrderContainer);
        }

        if(exchangeCondition.getAskPrice() != 0){
            for(int i = 0; i < exchangeCondition.getTiers(); i++) {
                Order order = createExchangeOrder(exchangeCondition.getSymbol(), exchangeCondition.getExchangeAccount(), Route.BUY, exchangeCondition.getAskPrice() - i, exchangeCondition.getVolume());
                askContainer.insertAsk(order);
                orderManager.addOrder(order);
            }
        }

        ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> bidSymbolOrderContainer = bidContainer.get(exchangeCondition.getSymbol());
        if(bidSymbolOrderContainer == null){
            bidSymbolOrderContainer = new ConcurrentSkipListMap();
            bidContainer.put(exchangeCondition.getSymbol(), bidSymbolOrderContainer);
        }

        if(exchangeCondition.getBidPrice() != 0){
            for(int i = 0; i < exchangeCondition.getTiers(); i++) {
                Order order = createExchangeOrder(exchangeCondition.getSymbol(), exchangeCondition.getExchangeAccount(), Route.SELL, exchangeCondition.getBidPrice() + i, exchangeCondition.getVolume());
                bidContainer.insertBid(order);
                orderManager.addOrder(order);
            }
        }
    }

    private Order createExchangeOrder(String symbol, Account exchangeAccount, Route route, Integer val, Integer volume) {
        return new Order(symbol, exchangeAccount.getAccountId(), route, val, volume);
    }

    public Account createAccount(int balance) {
        String accountId = "ACCOUNT_ID_" + RandomStringUtils.randomAlphabetic(10).toUpperCase();
        Account account = new Account(accountId, balance);
        accountManager.addNewAccount(account);
        return account;
    }

    public Order sendNewOrder(TestScenario testScenario, Route route, Integer val, Integer volume) {
        NewOrder newOrder = toNewOrder(testScenario.getSymbol(), route.name(), val, volume);
        testScenario.addNewOrder(newOrder);
        Order order = orderManager.manageNew(newOrder, testScenario.getAccount());
        testScenario.addOrder(order);
        return order;
    }

    public CloseShortPosition manageCloseShort(TestScenario testScenario, String symbol, Integer amount) {
        CloseShortPositionRequest request = new CloseShortPositionRequest();
        request.setSymbol(symbol);
        request.setAmount(amount);
        return closeShortManager.manageCloseShort(request, testScenario.getAccount());
    }

    private NewOrder toNewOrder(String symbol, String route, Integer val, Integer volume){
        NewOrder newOrder = new NewOrder();
        newOrder.setSymbol(symbol);
        newOrder.setRoute(route);
        newOrder.setVal(val);
        newOrder.setVolume(volume);
        return newOrder;
    }

}
