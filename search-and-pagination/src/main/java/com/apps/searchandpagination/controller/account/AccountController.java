package com.apps.searchandpagination.controller.account;

import com.apps.searchandpagination.controller.PageWrapper;
import com.apps.searchandpagination.persistance.entity.AccountData;
import com.apps.searchandpagination.persistance.query.account.AccountDataCriteria;
import com.apps.searchandpagination.service.SearchKeeper;
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

    private AccountDataService accountDataService;
    private SearchKeeper searchKeeper;

    @GetMapping({"accountdetails/"})
    public String getDetails(
            @RequestParam("detailId") String accountId,
            Model model) {
        model.addAttribute("accountdata", accountDataService.getAccountData(accountId));
        return "account/accountdetails :: accountdetails";
    }

    @GetMapping({"accountdatatable/"})
    public String accountDataTable(
            Model model,
            @RequestParam("page") Optional<Integer> optCurrentPage,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = optCurrentPage.orElse(0);
        int pageSize = size.orElse(10);

        Page<AccountData> dataPage = accountDataService.findAccounts(PageRequest.of(currentPage, pageSize), Optional.empty());

        PageWrapper<AccountData> page = new PageWrapper<>(dataPage, constructUrl(Optional.empty()));
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        return "account/accountdatatable :: accountdatatable";
    }

    private String constructUrl(Optional<AccountDataCriteria> criteria){
        if(!criteria.isPresent()){
            return "accountdatatable/?searchid=";
        } else {
            String searchId = searchKeeper.addSearchCriteria(criteria.get());
            return "accountdatatable/?searchid=" + searchId;
        }
    }

    @Autowired
    public void setAccountDataService(AccountDataService accountDataService) {
        this.accountDataService = accountDataService;
    }

    @Autowired
    public void setSearchKeeper(SearchKeeper searchKeeper) {
        this.searchKeeper = searchKeeper;
    }
}
