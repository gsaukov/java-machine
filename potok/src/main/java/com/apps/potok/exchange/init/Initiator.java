package com.apps.potok.exchange.init;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.exchange.core.AskComparator;
import com.apps.potok.exchange.core.AskContainer;
import com.apps.potok.exchange.core.BidContainer;
import com.apps.potok.exchange.core.ExchangeApplication;
import com.apps.potok.exchange.core.Order;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.exchange.core.Position;
import com.apps.potok.exchange.core.SymbolContainer;
import com.apps.potok.exchange.core.Route;
import com.apps.potok.soketio.model.execution.Deposit;
import com.apps.potok.soketio.model.order.NewOrder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ThreadLocalRandom;

import static com.apps.potok.exchange.core.Route.BUY;
import static com.apps.potok.exchange.core.Route.SELL;
import static com.apps.potok.exchange.core.Route.SHORT;

@Service
public class Initiator {

    public final static String MK_MAKER = "MK_MAKER";
    public final static String TEST_ACCOUNT_ID = "TEST_ACCOUNT_ID";

    @Value("${exchange.symbol-size}")
    private Integer symbolSize;

    @Value("${exchange.price.min-range}")
    private Integer minRange;

    @Value("${exchange.price.max-range}")
    private Integer maxRange;

    @Value("${exchange.accounts-size}")
    private Integer accountsSize;

    private final SymbolContainer symbolContainer;
    private final AccountManager accountManager;

    private final AskContainer askContainer;
    private final BidContainer bidContainer;
    private final ExchangeApplication exchangeApplication;
    private final OrderManager orderManager;

    public Initiator(SymbolContainer symbolContainer, AccountManager accountManager, OrderManager orderManager,
                     AskContainer askContainer, BidContainer bidContainer, ExchangeApplication exchangeApplication) {
        this.symbolContainer = symbolContainer;
        this.accountManager = accountManager;
        this.askContainer = askContainer;
        this.bidContainer = bidContainer;
        this.exchangeApplication = exchangeApplication;
        this.orderManager = orderManager;
    }

    public void initiate (){
        createSymbols ();
        createAskContainer ();
        createBidContainer ();
        createAccounts ();
    }

    private void createSymbols (){
        for(int i = 0 ; i < symbolSize ; i++){
            symbolContainer.addSymbol(RandomStringUtils.randomAlphabetic(4).toUpperCase(), RandomUtils.nextInt(minRange, maxRange));
        }
    }

    private void createAskContainer (){
        for (String symbol : symbolContainer.getSymbols()) {
            ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> symbolOrderContainer = askContainer.get(symbol);
            if(symbolOrderContainer == null){
                symbolOrderContainer = new ConcurrentSkipListMap(new AskComparator());
                askContainer.put(symbol, symbolOrderContainer);
            }
        }
    }

    private void createBidContainer (){
        for (String symbol : symbolContainer.getSymbols()) {
            ConcurrentSkipListMap<Integer, ConcurrentLinkedDeque<Order>> symbolOrderContainer = bidContainer.get(symbol);
            if(symbolOrderContainer == null){
                symbolOrderContainer = new ConcurrentSkipListMap();
                bidContainer.put(symbol, symbolOrderContainer);
            }
        }
    }

    private void createAccounts (){
        for(int i = 0 ; i < accountsSize ; i++){
            String accountId = RandomStringUtils.randomAlphabetic(9).toUpperCase();
            Account account = new Account(accountId, RandomUtils.nextLong(1000000l, 10000000l));
            accountManager.addNewAccount(account);
            createPositions(account, 60, 100);
            createOrders(account);
        }
        Account mkMaker = new Account(MK_MAKER, 9999999999l);
        accountManager.addNewAccount(mkMaker);
        createMkMakerPositions(mkMaker);
        Account testAccount = new Account(TEST_ACCOUNT_ID, 10000000l);
        accountManager.addNewAccount(testAccount);
    }

    private void createPositions(Account account, int number, int volume){
        Set<String> symbols = getRandomSymbols(number);
        for (String symbol : symbols) {
            account.doExecution(createDeposit(account.getAccountId(), symbol, volume));
        }
    }

    private Deposit createDeposit(String accountId, String symbol, int avgVolume){
        UUID depositUuid = UUID.randomUUID();
        Date timestamp = new Date();
        Route route = getRoute();
        Integer val = getVal(symbol);
        Integer volume = RandomUtils.nextInt(1, avgVolume) * 10;
        Integer blockedPrice = 0;
        if (SHORT.equals(route)) {
            blockedPrice = orderManager.getShortBlockedPrice(symbol);
        }
        return new Deposit(depositUuid, timestamp, symbol, accountId, route, val, blockedPrice, volume);
    }

    private void createOrders(Account account){
        createBuyOrders(account);
        createSellOrders(account);
        //todo createShortOrders.
    }

    private void createBuyOrders(Account account) {
        Set<String> symbols = getRandomSymbols(15);
        for (String symbol : symbols) {
            Integer val = getVal(symbol, BUY);
            Integer volume = RandomUtils.nextInt(1, 100) * 10;
            NewOrder newOrder = toNewOrder(symbol, BUY.name(), val, volume);
            exchangeApplication.manageNew(newOrder, account);
        }
    }

    private void createSellOrders(Account account) {
        List<Position> positions = new ArrayList<>(account.getPositions());
        for (Position position : positions) {
            if(RandomUtils.nextInt(0, 9) < 5){
                continue; //only ~60% chance of a sell order on existing position
            }
            Integer val = getVal(position.getSymbol(), SELL);
            Integer volume = (RandomUtils.nextInt(10, position.getVolume()) / 10) * 10 ;
            NewOrder newOrder = toNewOrder(position.getSymbol(), BUY.name(), val, volume);
            exchangeApplication.manageNew(newOrder, account);
        }
    }

    private Set<String> getRandomSymbols(int number) {
        Set<String> symbols = new HashSet<>();
        for (int i = 0; i < number; i++) {
            symbols.add(getRandomSymbol());
        }
        return symbols;
    }

    private String getRandomSymbol() {
        return symbolContainer.get(RandomUtils.nextInt(0, symbolContainer.getSymbols().size()));
    }

    private Route getRoute () { //Deposit position can be only buy or short
        if(RandomUtils.nextInt(0, 9) < 8){
            return BUY; //90%
        } else {
            return SHORT; //10%
        }
    }

    private Integer getVal(String symbol, Route route) {
        Integer val = symbolContainer.getQuote(symbol);
        if(BUY.equals(route)){
            return RandomUtils.nextInt(1, val);
        } else {
            return RandomUtils.nextInt(val - 1, 100);
        }
    }

    private Integer getVal (String symbol) {
        Integer val = symbolContainer.getQuote(symbol);
        return getDynamicPrice(val);
    }

    private Integer getDynamicPrice (Integer val) {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        int coefficient = Math.toIntExact(Math.round(val * 0.1d + 0.5 + r.nextGaussian()));
        if(RandomUtils.nextBoolean()){
            return val - coefficient;
        } else {
            return val + coefficient;
        }
    }

    private void createMkMakerPositions (Account account) {
        for(String symbol : symbolContainer.getSymbols()) {
            Deposit deposit = new Deposit(UUID.randomUUID(), new Date(), symbol, account.getAccountId(), BUY, getVal(symbol), 0, 10000 );
            account.doExecution(deposit);
        }
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
