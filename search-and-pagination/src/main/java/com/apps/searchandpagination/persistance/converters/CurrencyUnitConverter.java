package com.apps.searchandpagination.persistance.converters;

import org.joda.money.CurrencyUnit;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CurrencyUnitConverter implements AttributeConverter<CurrencyUnit, String> {

    @Override
    public String convertToDatabaseColumn(CurrencyUnit currencyUnit) {
        if(currencyUnit == null){
            return null;
        }
        return currencyUnit.getCode();
    }

    @Override
    public CurrencyUnit convertToEntityAttribute(String currency) {
        if(currency == null){
            return null;
        }
        return CurrencyUnit.of(currency);
    }


}