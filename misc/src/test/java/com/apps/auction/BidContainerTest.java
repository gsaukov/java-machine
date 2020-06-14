package com.apps.auction;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class BidContainerTest {

    private BidContainer container = new BidContainer();

    @BeforeClass
    void populateContainer(){
        container.addBid(new Bid(2, 3, container));
        container.addBid(new Bid(3, 3, container));
        container.addBid(new Bid(4, 1, container));
    }

    @Test
    void testRankByBidFirstPrice() {
        assertEquals(container.getAllByBid().first().getBid(), Integer.valueOf(4));
    }

    @Test
    void testRankByRevenueFirstPrice() {
        assertEquals(container.getAllByRevenue().first().getBid(), Integer.valueOf(3));
    }

    @Test
    void testRankByBidSecondPrice() {
        assertEquals(container.getAllByBid().first().getSecondPriceByBid(), Integer.valueOf(4));
    }

    @Test
    void testRankByRevenueSecondPrice() {
        assertEquals(container.getAllByRevenue().first().getSecondPriceByBid(), Integer.valueOf(4));
    }

}