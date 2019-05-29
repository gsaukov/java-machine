package com.apps.searchandpagination.controller;

import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.service.TradeDataService;
import com.apps.searchandpagination.service.TradeDetailsService;
import com.apps.searchandpagination.service.TradeSearchConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class TradeController {

    private TradeDataService tradeDataService;
    private TradeDetailsService tradeDetailsService;
    private TradeSearchConverter tradeSearchConverter;


    @GetMapping({"/"})
    public String home(
            Model model,
            @RequestParam("page") Optional<Integer> optCurrentPage,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = optCurrentPage.orElse(0);
        int pageSize = size.orElse(10);

        Page<TradeData> dataPage = tradeDataService.findTrades(PageRequest.of(currentPage, pageSize), Optional.empty());

        PageWrapper<TradeData> page = new PageWrapper<TradeData>(dataPage, "getpage");
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("tradeSearchRequest", new TradeSearchRequest());
        return "home";
    }

    @PostMapping("/tradesearch")
    public String newEntry(@ModelAttribute TradeSearchRequest request) {
        tradeSearchConverter.convert(request);
        return "tradedatatable :: tradedatatable";
    }

    @GetMapping({"getpage/"})
    public String getPage(
            Model model,
            @RequestParam("page") Optional<Integer> optCurrentPage,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = optCurrentPage.orElse(0);
        int pageSize = size.orElse(10);

        Page<TradeData> dataPage = tradeDataService.findTrades(PageRequest.of(currentPage, pageSize), Optional.empty());

        PageWrapper<TradeData> page = new PageWrapper<TradeData>(dataPage, "getpage");
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        return "tradedatatable :: tradedatatable";
    }

    @GetMapping({"details/"})
    public String getDetails(
            @RequestParam("detailId") String detailId,
            Model model) {
        model.addAttribute("transaction", tradeDetailsService.getTrade(detailId));
        return "tradedetails :: tradedetails";
    }

    @Autowired
    public void setTradeDataService(TradeDataService tradeDataService) {
        this.tradeDataService = tradeDataService;
    }

    @Autowired
    public void setTradeDetailsService(TradeDetailsService transactionService) {
        this.tradeDetailsService = transactionService;
    }

    @Autowired
    public void setTradeSearchConverter(TradeSearchConverter tradeSearchConverter) {
        this.tradeSearchConverter = tradeSearchConverter;
    }
}
