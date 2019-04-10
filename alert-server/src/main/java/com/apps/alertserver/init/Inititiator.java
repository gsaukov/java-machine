package com.apps.alertserver.init;


import com.apps.alertserver.Alert;
import com.apps.alertserver.MkData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.apps.alertserver.Alert.Route.BUY;


public class Inititiator {

    public static void initiateContainer (int size, HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> alertContainer, List<String> symbols){
        for(Alert alert : getAlerts(size, symbols)){
            insertAllert(alertContainer, alert);
        }
    }

    private static void insertAllert(HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> allAlertContainer, Alert alert) {
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> symbolAlertContainer = allAlertContainer.get(alert.getSymbol());


        if(symbolAlertContainer == null){
            symbolAlertContainer = new ConcurrentSkipListMap();
            allAlertContainer.put(alert.getSymbol(), symbolAlertContainer);
        }

        insertPrice(symbolAlertContainer, alert);
    }

    private static void insertPrice(ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> symbolAlertContainer, Alert alert) {
        CopyOnWriteArrayList<String> customerContainer = symbolAlertContainer.get(alert.getVal());

        if(customerContainer == null){
            customerContainer = new CopyOnWriteArrayList();
            symbolAlertContainer.put(alert.getVal(), customerContainer);
        }

        customerContainer.add(alert.getAccount());

    }

    private static List<Alert> getAlerts(int size, List<String> symbols) {
        List<String> customers = getCustomers(size);
        List<Integer> vals = getVals(size);

        List<Alert> alerts = new ArrayList<>();
        for(int i = 0 ; i < size; i++){
            String customer = customers.get(RandomUtils.nextInt(0, customers.size()));
            String symbol = symbols.get(RandomUtils.nextInt(0, symbols.size()));
            Integer val = vals.get(RandomUtils.nextInt(0, vals.size()));

            Alert alert = new Alert(symbol, customer, BUY, val);

            alerts.add(alert);
        }
        return alerts;
    }

    private static List<String> getCustomers(int size) {
        List<String> customers = new ArrayList<>();

        for(int i = 0 ; i < size/5 ; i++){
            customers.add(RandomStringUtils.randomAlphabetic(3) + " " + RandomStringUtils.randomAlphabetic(5));
        }

        return customers;
    }

    public static List<String> getSymbols(int size) {
        List<String> symbols = new ArrayList<>();

        for(int i = 0 ; i < size/10 ; i++){
            symbols.add(RandomStringUtils.randomAlphabetic(4).toUpperCase());
        }

        return symbols;
    }

    private static List<Integer> getVals(int size) {
        List<Integer> vals = new ArrayList<>();

        for(int i = 0 ; i < size/2 ; i++){
            vals.add(RandomUtils.nextInt(50, 100));
        }

        return vals;
    }

    public static List<MkData> getMkData(int size, List<String> symbols){
        List<MkData> mkDatas = new ArrayList<>();

        for(int i = 0 ; i < size; i++){
            String symbol = symbols.get(RandomUtils.nextInt(0, symbols.size()));

            MkData mkData = new MkData (symbol, RandomUtils.nextInt(30, 90));

            mkDatas.add(mkData);
        }
        return mkDatas;
    }

}
