package com.apps.searchandpagination.controller.trade;

import com.apps.searchandpagination.controller.PageWrapper;
import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.query.trade.TradeDetailsCriteria;
import com.apps.searchandpagination.service.SearchKeeper;
import com.apps.searchandpagination.service.trade.TradeDataService;
import com.apps.searchandpagination.service.trade.TradeDetailsService;
import com.apps.searchandpagination.service.trade.TradeSearchConverter;
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
    private SearchKeeper searchKeeper;

    @GetMapping({"/"})
    public String home(
            Model model,
            @RequestParam("page") Optional<Integer> optCurrentPage,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = optCurrentPage.orElse(0);
        int pageSize = size.orElse(10);

        Page<TradeData> dataPage = tradeDataService.findTrades(PageRequest.of(currentPage, pageSize), Optional.empty());

        PageWrapper<TradeData> page = new PageWrapper<TradeData>(dataPage, constructUrl(Optional.empty()));
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        model.addAttribute("tradeSearchRequest", new TradeSearchRequest());
        return "home";
    }

    @PostMapping("/tradesearch")
    public String tradeSearch (Model model, @ModelAttribute TradeSearchRequest request) {
        Optional<TradeDetailsCriteria> criteria = tradeSearchConverter.convert(request);
        Page<TradeData> dataPage = tradeDataService.findTrades(PageRequest.of(0, Integer.valueOf(request.getItemsSize())), criteria);
        PageWrapper<TradeData> page = new PageWrapper<TradeData>(dataPage, constructUrl(criteria));
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        return "trade/tradedatatable :: tradedatatable";
    }

    @GetMapping({"tradesearchpage/"})
    public String getTradeSearchPage(
            Model model,
            @RequestParam("searchid") String searchId,
            @RequestParam("page") Optional<Integer> optCurrentPage,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = optCurrentPage.orElse(0);
        int pageSize = size.orElse(10);
        Optional<TradeDetailsCriteria> criteria = searchKeeper.getSearchCriteria(searchId);
        Page<TradeData> dataPage = tradeDataService.findTrades(PageRequest.of(currentPage, pageSize), criteria);

        PageWrapper<TradeData> page = new PageWrapper<TradeData>(dataPage, constructUrl(criteria));
        model.addAttribute("page", page);
        model.addAttribute("dataPage", dataPage);
        return "trade/tradedatatable :: tradedatatable";
    }

    @GetMapping({"tradedetails/"})
    public String getDetails(
            @RequestParam("detailId") String detailId,
            Model model) {
        model.addAttribute("transaction", tradeDetailsService.getTrade(detailId));
        return "trade/tradedetails :: tradedetails";
    }

    private String constructUrl(Optional<TradeDetailsCriteria> criteria){
        if(!criteria.isPresent()){
            return "tradesearchpage/?searchid=";
        } else {
            String searchId = searchKeeper.addSearchCriteria(criteria.get());
            return "tradesearchpage/?searchid=" + searchId;
        }
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

    @Autowired
    public void setSearchKeeper(SearchKeeper searchKeeper) {
        this.searchKeeper = searchKeeper;
    }

}
