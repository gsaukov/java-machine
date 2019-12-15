package com.apps.potok.exchange.init;

import com.apps.potok.exchange.account.Account;
import com.apps.potok.exchange.account.AccountManager;
import com.apps.potok.exchange.core.OrderManager;
import com.apps.potok.exchange.core.SymbolContainer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

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

    public InitiatorV2(SymbolContainer symbolContainer, AccountManager accountManager, OrderManager orderManager) {
        this.symbolContainer = symbolContainer;
        this.accountManager = accountManager;
        this.orderManager = orderManager;
    }

    private void createSymbols (){
        for(int i = 0 ; i < symbolSize ; i++){
            symbolContainer.addSymbol(RandomStringUtils.randomAlphabetic(4).toUpperCase(), RandomUtils.nextInt(minRange, maxRange));
        }
    }

    private void createAccounts (){
        for(int i = 0 ; i < accountsSize ; i++){
            String accountId = RandomStringUtils.randomAlphabetic(9).toUpperCase();
            Account account = new Account(accountId, RandomUtils.nextLong(100000l, 10000000l));
            accountManager.addNewAccount(account);
        }
        Account mkMaker = new Account(MK_MAKER, 9999999999l);
        accountManager.addNewAccount(mkMaker);
        Account testAccount = new Account(TEST_ACCOUNT_ID, 10000000l);
        accountManager.addNewAccount(testAccount);
    }


}
