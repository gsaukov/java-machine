package com.apps.searchandpagination.service;

import com.apps.searchandpagination.controller.TradeSearchRequest;
import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.query.trade.TradeDetailsCriteria;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeSearchConverter {

    public TradeDetailsCriteria convert(TradeSearchRequest request){
        TradeDetailsCriteria criteria = new TradeDetailsCriteria();

        criteria.setIds(parseIn(request.getIds()));
        criteria.setSymbols(parseIn(request.getSymbols()));
        criteria.setAccounts(parseIn(request.getAccounts()));
        criteria.setRoute(pasrseRoute(request.getRoute()));
        criteria.setAmountGreater(pasrseBigMoney(request.getAmountGreater(), request.getCurrency()));
        criteria.setAmountLess(pasrseBigMoney(request.getAmountLess(), request.getCurrency()));
//        criteria.setDateAfter(String dateAfter);
//        criteria.setDateBefore(String dateBefore);
        criteria.setIban(request.getIban());
        criteria.setFirstName(request.getFirstName());
        criteria.setFirstNameComparisonType(pasrseComparisonType(request.getFirstNameComparisonType()));
        criteria.setLastName(request.getLastName());
        criteria.setLastNameComparisonType(pasrseComparisonType(request.getFirstNameComparisonType()));
        criteria.setOrder(pasrseOrder(request.getOrder()));
        return criteria;
    }

    private List<String> parseIn(String input){
        if(input == null){
            return null;
        }
        return Arrays.stream(input.split(",")).map(String::trim).collect(Collectors.toList());
    }

    private TradeData.Route pasrseRoute(String input){
        if(input == null){
            return null;
        }
        return TradeData.Route.valueOf(input);
    }

    private BigMoney pasrseBigMoney(String input, String currency){
        if(input == null){
            return null;
        }
        return BigMoney.of(CurrencyUnit.of(input), new BigDecimal(currency));
    }

    private TradeDetailsCriteria.ComparisonType pasrseComparisonType(String input){
        if(input == null){
            return null;
        } else if (input.equals("%")){
            return TradeDetailsCriteria.ComparisonType.LIKE;
        } else {
            return TradeDetailsCriteria.ComparisonType.EQUAL;
        }
    }

    private TradeDetailsCriteria.Order pasrseOrder(String input){
        if(input == null){
            return null;
        }
        return TradeDetailsCriteria.Order.valueOf(input);
    }
}

