package com.apps.depositary.controller;

import com.apps.depositary.persistance.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DepositaryController {

    @Autowired
    DepositRepository depositRepository;

    @ResponseBody
    @GetMapping({"/v1/depositary/account/{accountId}/deposits"})
    public Object getDeposits( @PathVariable("accountId") String accountId) {
        return depositRepository.findByAccountId(accountId);
    }
}
