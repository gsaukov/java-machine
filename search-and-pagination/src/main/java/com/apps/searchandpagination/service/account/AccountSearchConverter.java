package com.apps.searchandpagination.service.account;

import com.apps.searchandpagination.controller.account.AccountSearchRequest;
import com.apps.searchandpagination.persistance.entity.TradeData;
import com.apps.searchandpagination.persistance.query.account.AccountDataCriteria;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountSearchConverter {

    public Optional<AccountDataCriteria> convert(AccountSearchRequest request){
        AccountDataCriteria criteria = new AccountDataCriteria();

        criteria.setAccounts(parseIn(request.getAccounts()));
        criteria.setAddresses(parseIn(request.getAddresses()));
        criteria.setEmail(request.getEmail());
        criteria.setFirstName(request.getFirstName());
        criteria.setFirstNameComparisonType(parseComparisonType(request.getFirstNameComparisonType()));
        criteria.setLastName(request.getLastName());
        criteria.setLastNameComparisonType(parseComparisonType(request.getLastNameComparisonType()));
        criteria.setCity(request.getCity());
        criteria.setState(request.getState());
        criteria.setPostalCode(request.getPostalCode());
        criteria.setOrder(pasrseOrder(request.getOrder()));
        return Optional.of(criteria);
    }

    private List<String> parseIn(String input){
        if(input == null || input.isEmpty()){
            return null;
        }
        return Arrays.stream(input.split(",")).map(String::trim).collect(Collectors.toList());
    }

    private AccountDataCriteria.ComparisonType parseComparisonType(String input){
        if(input == null || input.isEmpty()){
            return null;
        } else if (input.equals("%")){
            return AccountDataCriteria.ComparisonType.LIKE;
        } else {
            return AccountDataCriteria.ComparisonType.EQUAL;
        }
    }

    private AccountDataCriteria.Order pasrseOrder(String input){
        if(input == null || input.isEmpty()){
            return null;
        }
        return AccountDataCriteria.Order.valueOf(input);
    }

}

