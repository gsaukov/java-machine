package com.apps.searchandpagination.controller.account;

import com.apps.searchandpagination.controller.PageWrapper;
import com.apps.searchandpagination.persistance.entity.AccountData;
import com.apps.searchandpagination.persistance.query.account.AccountDataCriteria;
import com.apps.searchandpagination.service.SearchKeeper;
import com.apps.searchandpagination.service.account.AccountDataService;
import com.apps.searchandpagination.service.account.AccountSearchConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AccountController {

    private AccountSearchConverter accountSearchConverter;
    private AccountDataService accountDataService;
    private SearchKeeper searchKeeper;

    @GetMapping({"accountdetails/"})
    public String getDetails(
            @RequestParam("detailId") String accountId,
            Model model) {
        model.addAttribute("accountdata", accountDataService.getAccountData(accountId));
        return "account/accountdetails :: accountdetails";
    }

    @GetMapping({"accounthome/"})
    public String accountHome(
            Model model,
            @RequestParam("page") Optional<Integer> optCurrentPage,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = optCurrentPage.orElse(0);
        int pageSize = size.orElse(10);

        Page<AccountData> dataPage = accountDataService.findAccounts(PageRequest.of(currentPage, pageSize), Optional.empty());

        PageWrapper<AccountData> page = new PageWrapper<>(dataPage, constructUrl(Optional.empty()));
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("accountSearchRequest", new  AccountSearchRequest());
        return "account/accounthome :: accounthome";
    }

    @GetMapping({"accountsearchpage/"})
    public String accountSearchPage(
            Model model,
            @RequestParam("searchid") String searchId,
            @RequestParam("page") Optional<Integer> optCurrentPage,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = optCurrentPage.orElse(0);
        int pageSize = size.orElse(10);

        Optional<AccountDataCriteria> criteria = searchKeeper.getAccountSearchCriteria(searchId);
        Page<AccountData> dataPage = accountDataService.findAccounts(PageRequest.of(currentPage, pageSize), criteria);

        PageWrapper<AccountData> page = new PageWrapper<>(dataPage, constructUrl(Optional.empty()));
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        return "account/accountdatatable :: accountdatatable";
    }

    @PostMapping("/accountsearch")
    public String accountSearch (Model model, @ModelAttribute AccountSearchRequest request) {
        Optional<AccountDataCriteria> criteria = accountSearchConverter.convert(request);
        Page<AccountData> dataPage = accountDataService.findAccounts(PageRequest.of(0, Integer.valueOf(request.getItemsSize())), criteria);
        PageWrapper<AccountData> page = new PageWrapper<>(dataPage, constructUrl(criteria));
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        return "account/accountdatatable :: accountdatatable";
    }


    private String constructUrl(Optional<AccountDataCriteria> criteria){
        if(!criteria.isPresent()){
            return "accountsearchpage/?searchid=";
        } else {
            String searchId = searchKeeper.addSearchCriteria(criteria.get());
            return "accountsearchpage/?searchid=" + searchId;
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

    @Autowired
    public void setAccountSearchConverter(AccountSearchConverter accountSearchConverter) {
        this.accountSearchConverter = accountSearchConverter;
    }

}
