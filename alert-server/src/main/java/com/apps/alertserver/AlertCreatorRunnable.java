package com.apps.alertserver;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AlertCreatorRunnable implements Runnable {

    private HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> alertContainer;
    private List<String> symbols;

    public AlertCreatorRunnable(HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> alertContainer, List<String> symbols) {
        this.alertContainer = alertContainer;
        this.symbols = symbols;
    }

    @Override
    public void run() {

            while (true){
                insertAllert(alertContainer, randomAlert());
                try {
                    Thread.sleep(RandomUtils.nextInt(0,30));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

    }

    private void insertAllert(HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> allAlertContainer, Alert alert) {
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> symbolAlertContainer = allAlertContainer.get(alert.getSymbol());
        insertPrice(symbolAlertContainer, alert);
    }

    private void insertPrice(ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> symbolAlertContainer, Alert alert) {
        CopyOnWriteArrayList<String> customerContainer = symbolAlertContainer.get(alert.getVal());
        if(customerContainer == null){
            customerContainer = new CopyOnWriteArrayList();
            symbolAlertContainer.put(alert.getVal(), customerContainer);
        }
        customerContainer.add(alert.getAccount());
    }
    
    private Alert randomAlert(){
        return new Alert(symbols.get(RandomUtils.nextInt(0, symbols.size())),
            "INS " + RandomStringUtils.randomAlphabetic(4), Alert.Route.BUY, RandomUtils.nextInt(50, 90));
    }
}
