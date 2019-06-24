package com.apps.searchandpagination.controller.account;

import com.apps.searchandpagination.controller.PageWrapper;
import com.apps.searchandpagination.service.account.AccountDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

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

    @GetMapping({"accountdatatable/"})
    public String home(
            Model model,
            @RequestParam("page") Optional<Integer> optCurrentPage,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = optCurrentPage.orElse(0);
        int pageSize = size.orElse(10);

        Page<TradeData> dataPage = accountDataService.findTrades(PageRequest.of(currentPage, pageSize), Optional.empty());

        PageWrapper<TradeData> page = new PageWrapper<TradeData>(dataPage, constructUrl(Optional.empty()));
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("tradeSearchRequest", new TradeSearchRequest());
        return "home";
    }
}
