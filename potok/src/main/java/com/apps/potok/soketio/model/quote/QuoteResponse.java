package com.apps.potok.soketio.model.quote;

import java.io.Serializable;
import java.util.List;

public class QuoteResponse implements Serializable {

    private final List<Quote> bidQuotes;
    private final List<Quote> askQuotes;

    public QuoteResponse(List<Quote> bidQuotes, List<Quote> askQuotes) {
        this.bidQuotes = bidQuotes;
        this.askQuotes = askQuotes;
    }

    public List<Quote> getBidQuotes() {
        return bidQuotes;
    }

    public List<Quote> getAskQuotes() {
        return askQuotes;
    }
}
