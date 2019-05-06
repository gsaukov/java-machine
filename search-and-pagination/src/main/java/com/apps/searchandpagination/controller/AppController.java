package com.apps.searchandpagination.controller;

import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.service.TradeDataService;
import com.apps.searchandpagination.service.TradeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class AppController {

    private TradeDataService tradeDataService;
    private TradeDetailsService tradeDetailsService;


    @GetMapping({"/"})
    public String home(
            Model model,
            @RequestParam("page") Optional<Integer> optCurrentPage,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = optCurrentPage.orElse(0);
        int pageSize = size.orElse(10);

        Page<TradeData> dataPage = tradeDataService.findPaginated(PageRequest.of(currentPage, pageSize));

        PageWrapper<TradeData> page = new PageWrapper<TradeData>(dataPage, "getpage");
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        return "home";
    }

    @GetMapping({"getpage/"})
    public String getPage(
            Model model,
            @RequestParam("page") Optional<Integer> optCurrentPage,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = optCurrentPage.orElse(0);
        int pageSize = size.orElse(10);

        Page<TradeData> dataPage = tradeDataService.findPaginated(PageRequest.of(currentPage, pageSize));

        PageWrapper<TradeData> page = new PageWrapper<TradeData>(dataPage, "getpage");
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        return "datatable :: datatable";
    }

    @GetMapping({"details/"})
    public String getDetails(
            @RequestParam("detailId") String detailId,
            Model model) {
        model.addAttribute("transaction", tradeDetailsService.getTransaction(detailId));
        return "transactiondetails :: transactiondetails";
    }

    @Autowired
    public void setTradeDataService(TradeDataService tradeDataService) {
        this.tradeDataService = tradeDataService;
    }

    @Autowired
    public void setTradeDetailsService(TradeDetailsService transactionService) {
        this.tradeDetailsService = transactionService;
    }
}
