package com.apps.depositary.persistance.repository;

import com.apps.depositary.service.SafeDeposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Set;

@Service
public class DepositUpdater {

    @Autowired
    private DepositRepository depositRepository;

    @Transactional
    public void updateDeposits(Set<SafeDeposit> updateBatch) {
        for(SafeDeposit deposit : updateBatch){
            BigDecimal fillPrice = new BigDecimal(deposit.getFillPrice().get(), MathContext.DECIMAL32);
            depositRepository.updateDeposit(fillPrice, deposit.getQuantity().get(), deposit.getUuid());
        }
    }

}
