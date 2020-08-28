package com.apps.potok;

import com.apps.potok.config.BaseTest;
import com.apps.potok.config.TestScenario;
import com.apps.potok.config.TestScenarioCreator;
import com.apps.potok.exchange.core.CancelOrderManager;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class AccountBalanceTest extends BaseTest {

    @Autowired
    private CancelOrderManager cancelOrderManager;

    @Autowired
    private TestScenarioCreator testScenarioCreator;

    private TestScenario testScenario;
    private final int BUY_PRICE = 5;
    private final int SHORT_PRICE = 6;
    private final int SELL_PRICE = 6;

    @BeforeMethod
    public void prepare() {
        testScenario = testScenarioCreator.newTestScenario();
    }

    @Test
    public void balanceCancelBuy() throws InterruptedException {
        long balance = testScenario.getBalance();
        Order order = testScenarioCreator.sendNewOrder(testScenario, Route.BUY, BUY_PRICE, 1);
        assertEquals(testScenario.getBalance(), balance - BUY_PRICE);
        cancelOrderManager.cancelOrder(order.getUuid(), testScenario.getAccountId());
        Thread.sleep(10);
        assertEquals(testScenario.getBalance(), balance);
    }

    @Test
    public void balanceCancelShort() throws InterruptedException {
        long balance = testScenario.getBalance();
        Order order = testScenarioCreator.sendNewOrder(testScenario, Route.SHORT, SHORT_PRICE, 1);
        assertEquals(testScenario.getBalance(), balance - order.getBlockedPrice());
        cancelOrderManager.cancelOrder(order.getUuid(), testScenario.getAccountId());
        Thread.sleep(10);
        assertEquals(testScenario.getBalance(), balance);
    }

    @Test
    public void balanceCancelSell() throws InterruptedException {
        //create position
        long balance = testScenario.getBalance();
        Order buyOrder = testScenarioCreator.sendNewOrder(testScenario, Route.BUY, SELL_PRICE, 1);
        long newBalance =  balance - SELL_PRICE;
        assertEquals(testScenario.getBalance(), newBalance);
        Thread.sleep(10);
        assertNotNull(testScenario.getAccount().getPosition(testScenario.getSymbol()));

        //sell position it does not affect balace
        Order sellOrder = testScenarioCreator.sendNewOrder(testScenario, Route.SELL, SELL_PRICE, 1);
        assertEquals(testScenario.getBalance(), newBalance);
        cancelOrderManager.cancelOrder(sellOrder.getUuid(), testScenario.getAccountId());
        Thread.sleep(10);
        assertEquals(testScenario.getBalance(), newBalance);
    }

    @Test
    public void balanceCancelPartFilledBuy() throws InterruptedException {
        int volume = 2;
        long balance = testScenario.getBalance();
        Order order = testScenarioCreator.sendNewOrder(testScenario, Route.BUY, SELL_PRICE, volume);
        assertEquals(order.getVolume().intValue(), 1);
        assertEquals(testScenario.getBalance(), balance - (SELL_PRICE * volume));
        cancelOrderManager.cancelOrder(order.getUuid(), testScenario.getAccountId());
        Thread.sleep(10);
        assertEquals(testScenario.getBalance(), balance - SELL_PRICE);
    }

    @Test
    public void balanceCancelPartFilledShort() throws InterruptedException {
        int volume = 2;
        long balance = testScenario.getBalance();
        Order order = testScenarioCreator.sendNewOrder(testScenario, Route.BUY, SHORT_PRICE, volume);
        assertEquals(order.getVolume().intValue(), 1);
        assertEquals(testScenario.getBalance(), balance - (SHORT_PRICE * volume));
        cancelOrderManager.cancelOrder(order.getUuid(), testScenario.getAccountId());
        Thread.sleep(10);
        assertEquals(testScenario.getBalance(), balance - SHORT_PRICE);
    }

}
