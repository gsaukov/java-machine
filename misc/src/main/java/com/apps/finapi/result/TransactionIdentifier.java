package com.apps.finapi.result;
// TODO: Review this code and comment on it: Is it correct? Is there something that could be improved?

import java.util.Objects;

/**
 * A wrapper for a transaction ID (as received from the bank), together with the bank account that the
 * transaction belongs to. You can use this this class to detect and filter out transactions that
 * we receive as duplicates from the bank, by using a Set<TransactionIdentifier>. Something like this:
 *
 * <p>
 * Set<TransactionIdentifier> uniqueTransactions = new HashSet<TransactionIdentifier>();
 * for(Account account : accounts){
 * for(Transaction transaction : account.getTransactions()){
 * TransactionIdentifier id = new TransactionIdentifier(account.getId(), transaction.getId());
 * uniqueTransactions.add(id);
 * }
 * }
 * -- Now, the set of 'uniqueTransactions' would have no duplicate transactions
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
