package com.apps.searchandpagination.service.trade;

import com.apps.searchandpagination.controller.analyse.AnalyseDomainRequest;
import com.apps.searchandpagination.controller.analyse.AnalysedDataResponse;
import com.apps.searchandpagination.controller.analyse.DomainPerformanceDataRequest;
import com.apps.searchandpagination.controller.analyse.DomainPerformanceDataResponse;
import com.apps.searchandpagination.persistance.entity.TradeDetails;
import com.apps.searchandpagination.persistance.repository.TradeDataRepository;
import com.apps.searchandpagination.persistance.repository.TradeDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class TradeDetailsService {

    private TradeDetailsRepository tradeDetailsRepository;

    private TradeDataRepository tradeDataRepository;

    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");

    @PostAuthorize("hasDomain(returnObject.domain)")
    public TradeDetails getTrade(String detailId) {
        return tradeDetailsRepository.findByTradeData(tradeDataRepository.findById(detailId).get());
    }

    public List<AnalysedDataResponse> findAggregatedSymbolsData(AnalyseDomainRequest request){
        List<TradeDetailsRepository.AnalysedData> analysedDatas =
                tradeDetailsRepository.findAggregatedSymbolsData(request.getDomain(), request.getSize());
        List<AnalysedDataResponse> response = new ArrayList<>();
        for(TradeDetailsRepository.AnalysedData analysedData : analysedDatas){
            AnalysedDataResponse analysedDataResponse = new AnalysedDataResponse();
            analysedDataResponse.setLabel(analysedData.getLabel());
            analysedDataResponse.setValue(analysedData.getValue());
            analysedDataResponse.setVolume(analysedData.getVolume());
            response.add(analysedDataResponse);
        }
        return response;
    }

    public List<DomainPerformanceDataResponse> findDomainPerformanceData(DomainPerformanceDataRequest request){
        List<TradeDetailsRepository.DomainPerformanceData> analysedDatas =
                tradeDetailsRepository.findDomainPerformanceData(request.getDomain(), request.getSize());
        List<DomainPerformanceDataResponse> response = new ArrayList<>();

        for(TradeDetailsRepository.DomainPerformanceData data : analysedDatas){
            DomainPerformanceDataResponse performanceDataresponse = new DomainPerformanceDataResponse();
            performanceDataresponse.setDate(formatter.format(data.getDate()));
            performanceDataresponse.setRoute(data.getRoute());
            performanceDataresponse.setSymbol(data.getSymbol());
            performanceDataresponse.setValue(data.getAmount().toBigInteger().intValue());
            performanceDataresponse.setVolume(data.getVal());
            response.add(performanceDataresponse);
        }
        return response;
    }

    public List<String> findAllDomains(){
        return tradeDetailsRepository.findAllDomains();
    }

    @Autowired
    public void setTradeDataRepository(TradeDataRepository tradeDataRepository) {
        this.tradeDataRepository = tradeDataRepository;
    }

    @Autowired
    public void setTradeDetailsService(TradeDetailsRepository tradeDetailsRepository) {
        this.tradeDetailsRepository = tradeDetailsRepository;
    }
}
