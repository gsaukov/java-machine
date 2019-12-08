package com.apps.potok;

import com.apps.potok.config.BaseTest;
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
import com.apps.potok.soketio.model.order.NewOrder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class AccountBalanceTest extends BaseTest {

    private Account account;
    private Account exchangeAccount;
    private String ACCOUNT_ID;
    private String SYMBOL;
    private final long balance = 10000l;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private ExchangeApplication exchangeApplication;

    @Autowired
    private Initiator initiator;

    @Autowired
    private SymbolContainer symbolContainer;

    @Autowired
    private AskContainer askContainer;

    @Autowired
    private BidContainer bidContainer;

    @BeforeMethod
    public void prepare() {
        this.account = createAccount();
        this.exchangeAccount = createAccount();
        prepareExchange();
    }

    @Test
    public void balanceCancelBuy() throws InterruptedException {
        int buyPrice = 5;
        int volume = 1;
        NewOrder newOrder = createNewOrder(Route.BUY, buyPrice, volume);
        Order order = exchangeApplication.manageNew(newOrder, account);
        assertEquals(account.getBalance(), balance - buyPrice);
        orderManager.cancelOrder(order.getUuid(), account.getAccountId());
        Thread.sleep(10);
        assertEquals(account.getBalance(), balance);
    }

    @Test
    public void balanceCancelShort() throws InterruptedException {
        int shortPrice = 6;
        int volume = 1;
        NewOrder newOrder = createNewOrder(Route.SHORT, shortPrice, volume);
        Order order = exchangeApplication.manageNew(newOrder, account);
        assertEquals(account.getBalance(), balance - shortPrice);
        orderManager.cancelOrder(order.getUuid(), account.getAccountId());
        Thread.sleep(10);
        assertEquals(account.getBalance(), balance);
    }

    @Test
    public void balanceCancelSell() throws InterruptedException {
        int sellPrice = 6;
        int volume = 1;
        NewOrder newBuyOrder = createNewOrder(Route.BUY, sellPrice, volume);
        Order buyOrder = exchangeApplication.manageNew(newBuyOrder, account);
        long newBalance = balance - sellPrice;
        assertEquals(account.getBalance(), newBalance);
        Thread.sleep(10);
        assertNotNull(account.getPosition(SYMBOL));

        NewOrder newOrder = createNewOrder(Route.SELL, sellPrice, volume);
        Order order = exchangeApplication.manageNew(newOrder, account);
        assertEquals(account.getBalance(), newBalance);
        orderManager.cancelOrder(order.getUuid(), account.getAccountId());
        Thread.sleep(10);
        assertEquals(account.getBalance(), newBalance);
    }

    @Test
    public void balanceCancelPartFilledBuy() throws InterruptedException {
        int buyPrice = 6;
        int volume = 2;
        NewOrder newOrder = createNewOrder(Route.BUY, buyPrice, volume);
        Order order = exchangeApplication.manageNew(newOrder, account);
        assertEquals(order.getVolume().intValue(), 1);
        assertEquals(account.getBalance(), balance - (buyPrice * volume));
        orderManager.cancelOrder(order.getUuid(), account.getAccountId());
        Thread.sleep(10);
        assertEquals(account.getBalance(), balance - buyPrice);
    }

    @Test
    public void balanceCancelPartFilledShort() throws InterruptedException {
        int shortPrice = 5;
        int volume = 2;
        NewOrder newOrder = createNewOrder(Route.SHORT, shortPrice, volume);
        Order order = exchangeApplication.manageNew(newOrder, account);
        assertEquals(order.getVolume().intValue(), 1);
        assertEquals(account.getBalance(), balance - (shortPrice * volume));
        orderManager.cancelOrder(order.getUuid(), account.getAccountId());
        Thread.sleep(10);
        assertEquals(account.getBalance(), balance - shortPrice);
    }

    // prepares new exchange conditions.
    // | ASK BUY   | BID SELL  |
    // | price qty | price qty |
    // | 5     1   | 6     1   |
    // | 4     1   | 7     1   |
    // | 3     1   | 8     1   |
    // | 2     1   | 9     1   |

    private void prepareExchange() {
        SYMBOL = "SYMBOL_" + RandomStringUtils.randomAlphabetic(10).toUpperCase();
        symbolContainer.addSymbol(SYMBOL, 5);
        for(int i = 0; i < 4; i++) {
            initiator.insertOrder(askContainer.get(), createExchangeOrder(Route.BUY, 5 - i, 1), Route.BUY);
        }

        for(int i = 0; i < 4; i++) {
            initiator.insertOrder(bidContainer.get(), createExchangeOrder(Route.SELL, 6 + i, 1), Route.SELL);
        }
    }

    private Account createAccount() {
        ACCOUNT_ID = "ACCOUNT_ID_" + RandomStringUtils.randomAlphabetic(10).toUpperCase();
        Account account = new Account(ACCOUNT_ID, 10000l);
        accountManager.addNewAccount(account);
        return account;
    }

    private Order createExchangeOrder(Route route, Integer val, Integer volume) {
        return new Order(SYMBOL, exchangeAccount.getAccountId(), route, val, volume);
    }

    private NewOrder createNewOrder(Route route, Integer val, Integer volume) {
        NewOrder newOrder = new NewOrder();
        newOrder.setRoute(route.name());
        newOrder.setSymbol(SYMBOL);
        newOrder.setVal(val);
        newOrder.setVolume(volume);
        return newOrder;
    }

}
