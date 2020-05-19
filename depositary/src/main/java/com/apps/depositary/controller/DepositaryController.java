package com.apps.depositary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DepositaryController {

    @ResponseBody
    @GetMapping({"depositary/account/getdeposits"})
    public String getDeposits(@RequestParam("accountId") String accountId) {
        return "";
    }
}
