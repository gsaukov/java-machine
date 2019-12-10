package com.apps.potok.config;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.exchange.core.AskContainer;
import com.apps.potok.exchange.core.BidContainer;
import com.apps.potok.exchange.core.ExchangeApplication;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.exchange.core.SymbolContainer;
import com.apps.potok.exchange.init.Initiator;
import com.apps.potok.exchange.mkdata.Route;
import com.apps.potok.soketio.model.execution.CloseShortPosition;
import com.apps.potok.soketio.model.execution.CloseShortPositionRequest;
import com.apps.potok.soketio.model.order.NewOrder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.List;

@TestComponent
public class TestScenarioCreator {

    @Autowired
    private Initiator initiator;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private SymbolContainer symbolContainer;

    @Autowired
    private AskContainer askContainer;

    @Autowired
    private BidContainer bidContainer;

    @Autowired
    private ExchangeApplication exchangeApplication;

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
        symbolContainer.addSymbol(exchangeCondition.getSymbol(), exchangeCondition.getAskPrice());
        if(exchangeCondition.getAskPrice() != 0){
            for(int i = 0; i < exchangeCondition.getTiers(); i++) {
                initiator.insertOrder(askContainer.get(), createExchangeOrder(exchangeCondition.getSymbol(), exchangeCondition.getExchangeAccount(), Route.BUY, exchangeCondition.getAskPrice() - i, exchangeCondition.getVolume()), Route.BUY);
            }
        }
        if(exchangeCondition.getBidPrice() != 0){
            for(int i = 0; i < exchangeCondition.getTiers(); i++) {
                initiator.insertOrder(bidContainer.get(), createExchangeOrder(exchangeCondition.getSymbol(), exchangeCondition.getExchangeAccount(), Route.SELL, exchangeCondition.getBidPrice() + i, exchangeCondition.getVolume()), Route.SELL);
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
        NewOrder newOrder = new NewOrder();
        newOrder.setRoute(route.name());
        newOrder.setSymbol(testScenario.getSymbol());
        newOrder.setVal(val);
        newOrder.setVolume(volume);
        testScenario.addNewOrder(newOrder);
        Order order = exchangeApplication.manageNew(newOrder, testScenario.getAccount());
        testScenario.addOrder(order);
        return order;
    }

    public CloseShortPosition manageCloseShort(TestScenario testScenario, String symbol, Integer amount) {
        CloseShortPositionRequest request = new CloseShortPositionRequest();
        request.setSymbol(symbol);
        request.setAmount(amount);
        return orderManager.manageCloseShort(request, testScenario.getAccount());
    }

}
