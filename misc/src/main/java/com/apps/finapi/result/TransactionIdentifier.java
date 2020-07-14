package com.apps.finapi.result;

import java.util.Objects;

/**
 * A wrapper for a transaction ID (as received from the bank), together with the bank account that the
 * transaction belongs to. You can use this this class to detect and filter out transactions that
 * we receive as duplicates from the bank.
 */
public class TransactionIdentifier {

    private final Long accountId;
    private final String transactionIdFromBank;

    public TransactionIdentifier(Long accountId, String transactionIdFromBank) {
        this.accountId = accountId;
        this.transactionIdFromBank = transactionIdFromBank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionIdentifier that = (TransactionIdentifier) o;
        return Objects.equals(accountId, that.accountId) &&
                Objects.equals(transactionIdFromBank, that.transactionIdFromBank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, transactionIdFromBank);
    }

}
