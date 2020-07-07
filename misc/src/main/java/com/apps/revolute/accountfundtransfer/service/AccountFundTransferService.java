package com.apps.revolute.accountfundtransfer.service;

import com.apps.revolute.accountfundtransfer.entity.Account;

import java.util.concurrent.locks.Lock;

import static com.apps.revolute.accountfundtransfer.service.TransferResult.FAIL;
import static com.apps.revolute.accountfundtransfer.service.TransferResult.OK;

public class AccountFundTransferService {

    public TransferResult transferFunds(Account from, Account to, Integer amount){
        Lock lock = from.getLock();
        TransferResult res;
        try {
            lock.lock();
            res = transferFundsThreadSafe(from, to, amount);
        } catch (Exception e) {
            System.out.println("Something went wrong. " + e.getMessage());
            res = FAIL;
        } finally {
            lock.unlock();
        }

        return res;
    }

    private TransferResult transferFundsThreadSafe(Account from, Account to, Integer amount){
        if (checkNegativeBalance(from, amount).equals(FAIL)){
            return FAIL;
        }
        from.getBalance().getAndAdd(-amount);
        to.getBalance().getAndAdd(amount);
        return OK;
    }

    private TransferResult checkNegativeBalance (Account from, Integer amount){
        if((from.getBalance().get() - amount) >= 0){
            return OK;
        } else {
            return FAIL;
        }
    }

}
