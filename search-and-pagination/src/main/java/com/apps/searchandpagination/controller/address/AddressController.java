package com.apps.searchandpagination.controller.address;

import com.apps.searchandpagination.service.account.AccountDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AddressController {

    private AccountDataService accountDataService;

    @ResponseBody
    @GetMapping(value = {"getallcities"}, produces = "application/json")
    public List<String> getAllCities() {
        return accountDataService.findAllCities();
    }

    @ResponseBody
    @GetMapping(value = {"getallstates"}, produces = "application/json")
    public List<String> getAllStates() {
        return accountDataService.findAllStates();
    }

    @Autowired
    public void setAccountDataService(AccountDataService accountDataService) {
        this.accountDataService = accountDataService;
    }

}
