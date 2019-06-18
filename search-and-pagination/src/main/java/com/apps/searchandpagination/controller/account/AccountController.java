package com.apps.searchandpagination.controller.account;

import com.apps.searchandpagination.service.account.AccountDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    @Autowired
    private AccountDataService accountDataService;

    @GetMapping({"accountdetails/"})
    public String getDetails(
            @RequestParam("detailId") String accountId,
            Model model) {
        model.addAttribute("accountdata", accountDataService.getAccountData(accountId));
        return "account/accountdetails :: accountdetails";
    }
}
