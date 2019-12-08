package com.apps.potok.config;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.exchange.core.AskContainer;
import com.apps.potok.exchange.core.BidContainer;
import com.apps.potok.exchange.core.ExchangeApplication;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.SymbolContainer;
import com.apps.potok.exchange.init.Initiator;
import com.apps.potok.exchange.mkdata.Route;
import com.apps.potok.soketio.model.order.NewOrder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class TestScenarioCreator {

    @Autowired
    private Initiator initiator;

    @Autowired
    private AccountManager accountManager;

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
        prepareExchange(symbol);
        return testScenario;
    }

    // prepares new exchange conditions.
    // | ASK BUY   | BID SELL  |
    // | price qty | price qty |
    // | 5     1   | 6     1   |
    // | 4     1   | 7     1   |
    // | 3     1   | 8     1   |
    // | 2     1   | 9     1   |

    public void prepareExchange(String symbol) {
        prepareExchange(symbol, 4, 5, 6, 1);
    }

    public void prepareExchange(String symbol, int tiers, int askPrice, int bidPrice, int volume) {
        symbolContainer.addSymbol(symbol, askPrice);
        Account exchangeAccount = createAccount(100000);
        for(int i = 0; i < tiers; i++) {
            initiator.insertOrder(askContainer.get(), createExchangeOrder(symbol, exchangeAccount, Route.BUY, askPrice - i, volume), Route.BUY);
        }

        for(int i = 0; i < tiers; i++) {
            initiator.insertOrder(bidContainer.get(), createExchangeOrder(symbol, exchangeAccount, Route.SELL, bidPrice + i, volume), Route.SELL);
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

}
