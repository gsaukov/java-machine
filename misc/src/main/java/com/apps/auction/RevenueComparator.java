package com.apps.auction;

import java.util.Comparator;

final class RevenueComparator implements Comparator<Bid> {

    @Override
    public int compare(Bid o1, Bid o2) {
        return o2.rankByRevenueScore().compareTo(o1.rankByRevenueScore());
    }
}
