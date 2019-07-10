package com.apps.searchandpagination.controller.symbol;

import com.apps.searchandpagination.service.trade.TradeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SymbolController {

    private TradeDataService tradeDataService;

    @ResponseBody
    @GetMapping({"getallsymbols"})
    public List<String> getAllSymbols() {
        return tradeDataService.findAllSymbols();
    }

    @Autowired
    public void setTradeDataService(TradeDataService tradeDataService) {
        this.tradeDataService = tradeDataService;
    }

}
