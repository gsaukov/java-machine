package com.apps.potok.server.alert;

import com.apps.potok.server.mkdata.MkData;
import com.apps.potok.server.mkdata.MkDataServer;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class AlertServer implements Runnable {

//    private HashMap<String, ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>>> alertContainer = new HashMap();
//    private List<String> symbols;
    AlertContainer alertContainer;
    MkDataServer mkDataServer;
    private int size = 10000;
    public static boolean RUN_SERVER = false;

    public AlertServer(AlertContainer alertContainer){
        this.alertContainer = alertContainer;
        this.mkDataServer = new MkDataServer(alertContainer);
//        symbols = Inititiator.getSymbols(size);
//        Inititiator.initiateContainer(size, alertContainer, symbols);
//        if(RUN_SERVER){
//            startCreatorThread();
//        }
    }

    @Override
    public void run() {
        while(true){
            fireAll(size * RandomUtils.nextInt(1, 3));
//            System.out.println("done");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void fireAll(int size) {
        List<MkData> events = mkDataServer.getMkData(size);
        for(MkData event : events){
            fireOne(event);
        }
    }

    private void fireOne(MkData event) {
        ConcurrentSkipListMap<Integer, CopyOnWriteArrayList<String>> map = alertContainer.get(event.getSymbol());

        SortedMap<Integer, CopyOnWriteArrayList<String>> toFire = map.headMap(event.getVal(), true);
        if (toFire.isEmpty()){
//            System.out.println(System.currentTimeMillis() + " nothing to FIRE for: " + event.getSymbol() + " at " + event.getVal());
            return;
        }

        for(Map.Entry<Integer, CopyOnWriteArrayList<String>> fired : toFire.entrySet()){
            for (String customer : fired.getValue()){
//                System.out.println(System.currentTimeMillis() + " FIRE: " + event.getSymbol() + " at " + event.getVal() + " for " + customer);
            }
        }

        SortedMap<Integer, CopyOnWriteArrayList<String>> toLeave = map.tailMap(event.getVal());
        alertContainer.put(event.getSymbol(), toLeave);
    }

//    private void startCreatorThread(){
//        Thread creatorThread = new Thread(new AlertCreatorServer(alertContainer));
//        creatorThread.setName("creatorThread");
//        creatorThread.setDaemon(true);
//        creatorThread.start();
//    }

}