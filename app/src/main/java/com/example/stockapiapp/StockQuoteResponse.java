package com.example.stockapiapp;

import com.google.gson.annotations.SerializedName;

public class StockQuoteResponse {
    @SerializedName("Global Quote")
    private StockQuote globalQuote;

    public StockQuote getGlobalQuote() {
        return globalQuote;
    }
}
