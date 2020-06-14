package com.apps.auction;

import java.util.TreeSet;

final public class Bid {

//    private final String bidId; // for persistence and uniqueness
//    private final String bidderId; //reference to bidder
    private final Integer bid;
    private final Integer ctr;
    private final BidContainer container;

    public Bid(Integer bid, Integer ctr, BidContainer container) {
        this.bid = bid;
        this.ctr = ctr;
        this.container = container;
    }

    public Integer rankByBidScore() {
        return bid;
    }
    public Integer rankByRevenueScore() {
        return bid * ctr;
    }

    public Integer getBid() {
        return bid;
    }

    public Integer getCtr() {
        return ctr;
    }

    public Integer getSecondPriceByBid() {
        return getSecondPrice(container.getAllByBid());
    }

    public Integer getSecondPriceByRevenue() {
        return getSecondPrice(container.getAllByRevenue());
    }

    private Integer getSecondPrice(TreeSet<Bid> bidTree) {
        Bid secondPriceCandidate = bidTree.lower(this);
        if(secondPriceCandidate != null){
            return secondPriceCandidate.getBid();
        } else {
            return bid;
        }
    }

}
