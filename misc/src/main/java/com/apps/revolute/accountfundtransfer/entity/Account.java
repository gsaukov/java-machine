package com.apps.revolute.accountfundtransfer.entity;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account implements Comparable<Account> {

    private final String accoutId;
    private final AtomicInteger balance;
    private final Lock lock;

    public Account(String accoutId, AtomicInteger balance) {
        this.accoutId = accoutId;
        this.balance = balance;
        this.lock = new ReentrantLock();
    }

    public String getAccoutId() {
        return accoutId;
    }

    public AtomicInteger getBalance() {
        return balance;
    }

    public Lock getLock() {
        return lock;
    }

    @Override
    public int compareTo(Account o) {
        return o.accoutId.compareTo(this.accoutId);
    }
}
