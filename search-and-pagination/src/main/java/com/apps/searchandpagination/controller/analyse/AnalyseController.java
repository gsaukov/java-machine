package com.apps.searchandpagination.controller.analyse;

import com.apps.searchandpagination.service.trade.TradeDataService;
import com.apps.searchandpagination.service.trade.TradeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AnalyseController {

    private TradeDataService tradeDataService;

    private TradeDetailsService tradeDetailsService;

    @GetMapping({"analysehome/"})
    public String accountHome(
            Model model) {
        model.addAttribute("analyseDomainRequest", new  AnalyseDomainRequest());
        return "analyse/analysehome :: analysehome";
    }

    @ResponseBody
    @PostMapping(value = {"analysedomain/"}, produces = "application/json")
    public List<AnalysedDataResponse> analysedomain(@ModelAttribute AnalyseDomainRequest request) {
        return tradeDetailsService.findAggregatedSymbolsData(request);
    }

    @ResponseBody
    @GetMapping(value = {"analysedomainperformance/"}, produces = "application/json")
    public List<DomainPerformanceDataResponse> domainPerformanceData(@ModelAttribute DomainPerformanceDataRequest request) {
        return tradeDetailsService.findDomainPerformanceData(request);
    }

    @ResponseBody
    @GetMapping(value = {"getalldomains"}, produces = "application/json")
    public List<String> getAllSymbols() {
        return tradeDetailsService.findAllDomains();
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
