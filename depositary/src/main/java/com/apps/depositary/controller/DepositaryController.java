package com.apps.depositary.controller;

import com.apps.depositary.persistance.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DepositaryController {

    @Autowired
    DepositRepository depositRepository;

    @ResponseBody
    @GetMapping({"depositary/account/getdeposits"})
    public Object getDeposits(@RequestParam("accountId") String accountId) {
        return depositRepository.findByAccountId(accountId);
    }
}
