package com.apps.potok;

import com.apps.potok.config.BaseTest;
import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.exchange.core.Exchange;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.mkdata.Route;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class AccountBalanceTest extends BaseTest {

    private Account account;
    private final String ACCOUNT_ID = "ACCOUNT_ID";
    private final String SYMBOL = "SYMBOL";

    @Autowired
    private Exchange exchange;

    @Autowired
    private AccountManager accountManager;

    @BeforeMethod
    public void prepare() {
        this.account = createAccount();
        prepareExchange();
    }

    @Test
    public void test() {
       System.out.println();
    }

    private void prepareExchange() {
        for(int i = 0; i < 4; i++) {
            exchange.fireOrder(createOrder(Route.BUY, 5 - i, 100));
        }

        for(int i = 0; i < 4; i++) {
            exchange.fireOrder(createOrder(Route.SELL, 6 + i, 100));
        }
    }

    private Account createAccount() {
        Account account = new Account(ACCOUNT_ID, 10000l);
        accountManager.addNewAccount(account);
        return account;
    }

    private Order createOrder(Route route, Integer val, Integer volume) {
        return new Order(SYMBOL, ACCOUNT_ID, route, val, volume);
    }

}
