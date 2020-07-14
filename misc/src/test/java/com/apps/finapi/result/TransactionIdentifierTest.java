package com.apps.finapi.result;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class TransactionIdentifierTest {
    private static final int TRN = 100;

    @Test
    public void testTransactionIdentifier() {
        Set<TransactionIdentifier> uniqueTransactions = new HashSet<>();
        for (long i = 0; i < TRN; i++) {
            for (long j = 0; j < TRN; j++) {
                TransactionIdentifier id = new TransactionIdentifier(i, Long.toString(j));
                uniqueTransactions.add(id);
            }
        }
        assertEquals(uniqueTransactions.size(), TRN * TRN);
    }

}
