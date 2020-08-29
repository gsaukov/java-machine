package com.apps.potok.exchange.randombehavior;

import com.apps.potok.exchange.account.BalanceCalculationStatistics;
import com.apps.potok.exchange.core.*;
import com.apps.potok.exchange.account.AccountManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.apps.potok.exchange.init.Initiator.MK_MAKER;
import static com.apps.potok.exchange.init.Initiator.TEST_ACCOUNT_ID;


@Service
public class AccountServerExecutor {

    private Logger logger = LoggerFactory.getLogger(AccountServerExecutor.class);

    @Value("${exchange.accountServer.threads:1}")
    private Integer accountServerThreads;

    @Autowired
    private SymbolContainer symbolContainer;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private CancelOrderManager cancelOrderManager;

    @Autowired
    private CloseShortManager closeShortManager;

    @Autowired
    protected ExchangeSpeed exchangeSpeed;

    @Autowired
    @Qualifier("potokServerRunner")
    private TaskExecutor executor;

    private final List<AccountServer> accountServers = new ArrayList<>();

    public void runAccountServers() {
        for (int i = 0; i < accountServerThreads; i++) {
            AccountServer accountServer = buildAccountServer(i);
            accountServers.add(accountServer);
            executor.execute(accountServer);
        }
    }

    public void stopAccountServers() {
        for (AccountServer accountServer : accountServers) {
            accountServer.stopExchangeServer();
        }
    }

    private AccountServer buildAccountServer(int serverNumber) {
        return new AccountServer(exchangeSpeed, symbolContainer, accountManager, orderManager, cancelOrderManager,
                closeShortManager, getAccountPartition(serverNumber));
    }

    private List<String> getAccountPartition(int serverNumber) {
        List<String> partitionAccountIds = new ArrayList<>();
        List<String> allAccountIds = accountManager.getAllAccountIds();
        int partitionSize = getPartitionSize(allAccountIds.size());
        int start = serverNumber * partitionSize;
        int end = Math.min(start + partitionSize, allAccountIds.size());
        for (int i = start; i < end; i++) {
            String accountId = allAccountIds.get(i);
            if (!MK_MAKER.equals(accountId) && !TEST_ACCOUNT_ID.equals(accountId)) {
                partitionAccountIds.add(accountId);
            }
        }
        return partitionAccountIds;
    }

    private int getPartitionSize(int accountsSize) {
        if (accountsSize % accountServerThreads == 0) {
            return accountsSize / accountServerThreads;
        } else {
            return (accountsSize / accountServerThreads) + 1;
        }
    }

}
