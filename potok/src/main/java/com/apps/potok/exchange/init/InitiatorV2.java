package com.apps.potok.exchange.init;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.exchange.core.SymbolContainer;
import com.apps.potok.exchange.mkdata.Route;
import com.apps.potok.soketio.model.execution.Deposit;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import static com.apps.potok.exchange.mkdata.Route.BUY;
import static com.apps.potok.exchange.mkdata.Route.SHORT;

@Service
public class InitiatorV2 {

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

    private SymbolContainer symbolContainer;
    private AccountManager accountManager;
    private OrderManager orderManager;

    private final AtomicLong askInit = new AtomicLong(0l);
    private final AtomicLong bidInit = new AtomicLong(0l);
    private final List<String> symbols = new ArrayList();

    public InitiatorV2(SymbolContainer symbolContainer, AccountManager accountManager, OrderManager orderManager) {
        this.symbolContainer = symbolContainer;
        this.accountManager = accountManager;
        this.orderManager = orderManager;
    }

    private void createSymbols (){
        for(int i = 0 ; i < symbolSize ; i++){
            symbolContainer.addSymbol(RandomStringUtils.randomAlphabetic(4).toUpperCase(), RandomUtils.nextInt(minRange, maxRange));
        }
        symbols.addAll(symbolContainer.getSymbols());
    }

    private void createAccounts (){
        for(int i = 0 ; i < accountsSize ; i++){
            String accountId = RandomStringUtils.randomAlphabetic(9).toUpperCase();
            Account account = new Account(accountId, RandomUtils.nextLong(100000l, 10000000l));
            createPositions(account);
            accountManager.addNewAccount(account);
        }
        Account mkMaker = new Account(MK_MAKER, 9999999999l);
        accountManager.addNewAccount(mkMaker);
        Account testAccount = new Account(TEST_ACCOUNT_ID, 10000000l);
        accountManager.addNewAccount(testAccount);
    }

    private void createPositions(Account account){
        int positionNumber = RandomUtils.nextInt(0, 30);
        Set<String> symbols = new HashSet<>();
        for (int i = 0; i < positionNumber; i++) {
            symbols.add(getRandomSymbol());
        }
        for (String symbol : symbols) {
            account.doExecution(createDeposit(account.getAccountId(), symbol));
        }
    }

    private Deposit createDeposit(String accountId, String symbol){
        UUID depositUuid = UUID.randomUUID();
        Date timestamp = new Date();
        Route route = getRoute();
        Integer val = getVal(symbol);
        Integer volume = RandomUtils.nextInt(0, 100) * 10;
        Integer blockedPrice = 0;
        if (SHORT.equals(route)) {
            blockedPrice = val + val / 10;
        }
        return new Deposit(depositUuid, timestamp, symbol, accountId, route, val, blockedPrice, volume);
    }

    private String getRandomSymbol() {
        return symbolContainer.getSymbols().get(RandomUtils.nextInt(0, symbols.size()));
    }

    private Route getRoute () { //Deposit position can be only buy or short
        if(RandomUtils.nextInt(0, 9) > 8){
            return BUY; //90%
        } else {
            return SHORT; //10%
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
}
