package com.apps.searchandpagination.service.trade;

import com.apps.searchandpagination.controller.trade.TradeSearchRequest;
import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.query.trade.TradeDetailsCriteria;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TradeSearchConverter {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public Optional<TradeDetailsCriteria> convert(TradeSearchRequest request){
        TradeDetailsCriteria criteria = new TradeDetailsCriteria();

        criteria.setTableId(request.getTableId());
        criteria.setIds(parseIn(request.getIds()));
        criteria.setSymbols(parseIn(request.getSymbols()));
        criteria.setAccounts(parseIn(request.getAccounts()));
        criteria.setRoute(pasrseRoute(request.getRoute()));
        criteria.setAmountGreater(pasrseBigMoney(request.getAmountGreater(), request.getCurrency()));
        criteria.setAmountLess(pasrseBigMoney(request.getAmountLess(), request.getCurrency()));
        criteria.setDateAfter(parseDate(request.getDateAfter()));
        criteria.setDateBefore(parseDate(request.getDateBefore()));
        criteria.setIban(request.getIban());
        criteria.setFirstName(request.getFirstName());
        criteria.setFirstNameComparisonType(parseComparisonType(request.getFirstNameComparisonType()));
        criteria.setLastName(request.getLastName());
        criteria.setLastNameComparisonType(parseComparisonType(request.getLastNameComparisonType()));
        criteria.setOrder(pasrseOrder(request.getOrder()));
        return Optional.of(criteria);
    }

    private List<String> parseIn(String input){
        if(input == null || input.isEmpty()){
            return null;
        }
        return Arrays.stream(input.split(",")).map(String::trim).collect(Collectors.toList());
    }

    private TradeData.Route pasrseRoute(String input){
        if(input == null || input.isEmpty()){
            return null;
        }
        return TradeData.Route.valueOf(input);
    }

    private BigMoney pasrseBigMoney(String input, String currency){
        if(input == null || input.isEmpty()){
            return null;
        }
        return BigMoney.of(CurrencyUnit.of(currency), new BigDecimal(input));
    }

    private TradeDetailsCriteria.ComparisonType parseComparisonType(String input){
        if(input == null || input.isEmpty()){
            return null;
        } else if (input.equals("%")){
            return TradeDetailsCriteria.ComparisonType.LIKE;
        } else {
            return TradeDetailsCriteria.ComparisonType.EQUAL;
        }
    }

    private TradeDetailsCriteria.Order pasrseOrder(String input){
        if(input == null || input.isEmpty()){
            return null;
        }
        return TradeDetailsCriteria.Order.valueOf(input);
    }

    private LocalDateTime parseDate(String input){
        if(input == null || input.isEmpty()){
            return null;
        }
        return LocalDate.parse(input, formatter).atStartOfDay();
    }
}

