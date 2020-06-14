package com.apps.auction;

import java.util.TreeSet;

public class BidContainer {

    private final TreeSet<Bid> byBid = new TreeSet<>(new BidComparator());
    private final TreeSet<Bid> byRevenue = new TreeSet<>(new RevenueComparator());

    public void addBid(Bid bid){
        this.byBid.add(bid);
        this.byRevenue.add(bid);
    }

    public TreeSet<Bid> getAllByBid() {
        return byBid;
    }

    public TreeSet<Bid> getAllByRevenue() {
        return byRevenue;
    }

}
