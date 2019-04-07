package com.apps.reflection;

//import org.joda.money.BigMoney;
//import org.joda.money.CurrencyUnit;

import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class RandomObjectFiller {

    private Random random = new Random();

    public <T> T createAndFill(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T instance = clazz.newInstance();
        for(Field field: clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = getRandomValueForField(field);
            field.set(instance, value);
        }
        return instance;
    }

    private Object getRandomValueForField(Field field) throws InstantiationException, IllegalAccessException  {
        Class<?> type = field.getType();
        // Note that we must handle the different types here! This is just an
        // example, so this list is not complete! Adapt this to your needs!
        if(type.isEnum()) {
            Object[] enumValues = type.getEnumConstants();
            return enumValues[random.nextInt(enumValues.length)];
        } else if(type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return random.nextInt();
        } else if(type.equals(Long.TYPE) || type.equals(Long.class)) {
            return random.nextLong();
        } else if(type.equals(Double.TYPE) || type.equals(Double.class)) {
            return random.nextDouble();
        } else if(type.equals(Float.TYPE) || type.equals(Float.class)) {
            return random.nextFloat();
        } else if(type.equals(BigDecimal.class)) {
            return new BigDecimal(random.nextDouble());
        } else if(type.equals(LocalDateTime.class)) {
            return LocalDateTime.now();
        } else if(type.equals(String.class)) {
            return RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        } else if(type.equals(BigInteger.class)){
            return BigInteger.valueOf(random.nextInt());
//        } else if (type.equals(BigMoney.class)) {
//            return BigMoney.of(CurrencyUnit.EUR, random.nextDouble());
        } else if(type.equals(LocalDate.class)) {
            return LocalDate.now();
        } else if (!type.isPrimitive()) {
            return createAndFill(type);
        }
        return createAndFill(type);
    }

}
