package com.apps.potok.server.alert;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class AlertCreatorServer extends Thread {

    private AlertContainer alertContainer;

    public AlertCreatorServer(AlertContainer alertContainer) {
        super.setDaemon(true);
        super.setName("alertCreatorThread");
        this.alertContainer = alertContainer;
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

    private void insertAllert(AlertContainer allAlertContainer, Alert alert) {
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
        List<String> symbols = alertContainer.getSymbols();
        return new Alert(symbols.get(RandomUtils.nextInt(0, symbols.size())),
            "INS " + RandomStringUtils.randomAlphabetic(4), Alert.Route.BUY, RandomUtils.nextInt(50, 90));
    }
}
