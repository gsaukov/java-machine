package com.apps.potok.soketio.model.quote;

import java.io.Serializable;
import java.util.List;

public class QuoteResponse implements Serializable {

    private final List<Quote> quotes;

    public QuoteResponse(List<Quote> quotes) {
        this.quotes = quotes;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }
}
