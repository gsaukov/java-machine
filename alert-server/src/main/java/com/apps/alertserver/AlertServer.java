package com.apps.alertserver;

import com.apps.alertserver.init.Inititiator;
import org.apache.commons.lang3.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AlertServer implements Runnable {

    private HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> alertContainer = new HashMap();
    private List<String> symbols;
    private int size = 10000;
    public static boolean RUN_SERVER = false;

    public AlertServer(){
        symbols = Inititiator.getSymbols(size);
        Inititiator.initiateContainer(size, alertContainer, symbols);
        if(RUN_SERVER){
            startCreatorThread();
        }
    }

    public void printSymbols (){
        for(Map.Entry<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> entry : alertContainer.entrySet()){
            String symbolName = entry.getKey();
            ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> prices = entry.getValue();
            Integer minPrice = prices.firstEntry().getKey();
            Integer maxPrice =  prices.lastEntry().getKey();
            System.out.println(" Symbol : " + symbolName + " price range is from " + minPrice + " to " + maxPrice);
        }
    }

    public void searchOffers(String symbolName, Integer maxDesirablePrice){
        if(!alertContainer.containsKey(symbolName)){
            System.out.println("Nothing found for symbol" + symbolName);
        }
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> offers = alertContainer.get(symbolName);
        printOffers(symbolName, offers.tailMap(maxDesirablePrice));
    }

    private void printOffers(String symbolName, Map<Integer, CopyOnWriteArrayList<String>> offers){
        for(Map.Entry<Integer, CopyOnWriteArrayList<String>> entry : offers.entrySet()){
            Integer value = entry.getKey();
            String userNames = "";
            for(String name : entry.getValue()){
                userNames += (" [" +  name + "],");
            }
            System.out.println(" Following offers for item: " + symbolName + " " + value + " users " + userNames);
        }
    }

    public List<String> getSymbols() {
        return symbols;
    }

    @Override
    public void run() {
        while(true){
            fireAll(size * RandomUtils.nextInt(1, 3));
            System.out.println("done");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void fireAll(int size) {
        List<MkData> events = Inititiator.getMkData(size, symbols);
        for(MkData event : events){
            fireOne(event);
        }
    }

    private void fireOne(MkData event) {
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> map = alertContainer.get(event.getSymbol());

        SortedMap<Integer, CopyOnWriteArrayList<String>> toFire = map.headMap(event.getVal(), true);
        if (toFire.isEmpty()){
            System.out.println(System.currentTimeMillis() + " nothing to FIRE for: " + event.getSymbol() + " at " + event.getVal());
            return;
        }

        for(Map.Entry<Integer, CopyOnWriteArrayList<String>> fired : toFire.entrySet()){
            for (String customer : fired.getValue()){
                System.out.println(System.currentTimeMillis() + " FIRE: " + event.getSymbol() + " at " + event.getVal() + " for " + customer);
            }
        }

        SortedMap<Integer, CopyOnWriteArrayList<String>> toLeave = map.tailMap(event.getVal());
        alertContainer.put(event.getSymbol(), new ConcurrentSkipListMap<>(toLeave));
    }

    private void startCreatorThread(){
        Thread creatorThread = new Thread(new AlertCreatorRunnable(alertContainer, symbols));
        creatorThread.setName("creatorThread");
        creatorThread.setDaemon(true);
        creatorThread.start();
    }

}