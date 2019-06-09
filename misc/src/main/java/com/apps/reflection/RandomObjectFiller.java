package com.apps.reflection;

import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class RandomObjectFiller {

    private RandomUtils random = new RandomUtils();

    public <T> T createAndFill(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T instance = clazz.newInstance();
        for(Field field: clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = getRandomValueForField(field, field.getType());
            field.set(instance, value);
        }
        return instance;
    }

    private Object getRandomValueForField(Field field, Class<?> type) throws InstantiationException, IllegalAccessException  {
        // Note that we must handle the different types here! This is just an
        // example, so this list is not complete! Adapt this to your needs!
        if(type.isEnum()) {
            Object[] enumValues = type.getEnumConstants();
            return enumValues[random.nextInt(0, enumValues.length)];
        } else if(type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return random.nextInt();
        } else if(type.equals(Long.TYPE) || type.equals(Long.class)) {
            return random.nextLong();
        } else if(type.equals(Double.TYPE) || type.equals(Double.class)) {
            return random.nextDouble();
        } else if(type.equals(Float.TYPE) || type.equals(Float.class)) {
            return random.nextFloat();
        } else if(type.equals(BigDecimal.class)) {
            return new BigDecimal(random.nextDouble(0, 1000));
        } else if(type.equals(LocalDateTime.class)) {
            return LocalDateTime.now().plusDays(random.nextInt(0, 10000)).minusDays(random.nextInt(0, 10000));
        } else if(type.equals(String.class)) {
            return RandomStringUtils.randomAlphanumeric(5, 20).toUpperCase();
        } else if(type.equals(BigInteger.class)){
            return BigInteger.valueOf(random.nextInt());
        } else if (type.equals(BigMoney.class)) {
            return BigMoney.of(CurrencyUnit.EUR, random.nextDouble(0, 1000));
        } else if(type.equals(LocalDate.class)) {
            return LocalDate.now().plusDays(random.nextInt(0, 10000)).minusDays(random.nextInt(0, 10000));
        } else if(type.equals(CurrencyUnit.class)) {
            return CurrencyUnit.EUR;
        } else if(type.isAssignableFrom(List.class)) {
            return createAndFillList(field);
        } else if(type.isAssignableFrom(HashMap.class)) {
            return createAndFillMap(field);
        } else if (!type.isPrimitive()) {
            return createAndFill(type);
        }
        return createAndFill(type);
    }

    private Collection createAndFillList(Field field) throws IllegalAccessException, InstantiationException {
        ParameterizedType integerListType = (ParameterizedType) field.getGenericType();
        Class<?> value = (Class<?>) integerListType.getActualTypeArguments()[0];
        List<Object> res = new ArrayList<>();
        for (int i = 0; i < random.nextInt(0, 20); i++){
            res.add(getRandomValueForField(null, value));
        }
        return res;
    }

    private HashMap createAndFillMap(Field field) throws IllegalAccessException, InstantiationException {
        ParameterizedType integerListType = (ParameterizedType) field.getGenericType();
        Class<?> key = (Class<?>) integerListType.getActualTypeArguments()[0];
        Class<?> value = (Class<?>) integerListType.getActualTypeArguments()[1];

        HashMap<Object, Object> res = new HashMap<>();
        for (int i = 0; i < random.nextInt(0, 20); i++){
            res.put(getRandomValueForField(null, key), getRandomValueForField(null, value));
        }
        return res;
    }

}
