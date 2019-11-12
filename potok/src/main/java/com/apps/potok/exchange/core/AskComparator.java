package com.apps.potok.exchange.core;

import java.util.Comparator;

public class AskComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2) * -1; //reverse
    }
}


