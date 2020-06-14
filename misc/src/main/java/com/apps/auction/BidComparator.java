package com.apps.auction;

import java.util.Comparator;

final class BidComparator implements Comparator<Bid> {

    @Override
    public int compare(Bid o1, Bid o2) {
        return o2.rankByBidScore().compareTo(o1.rankByBidScore());
    }
}
