package com.example.stockapiapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AlphaVantageApi {
    @Headers({
            "X-RapidAPI-Host: alpha-vantage.p.rapidapi.com",
            "X-RapidAPI-Key: 6ecbb8ad04msh133d6b5fd723deep1f794bjsn8209b9cfde7e"
    })

    @GET("query?function=GLOBAL_QUOTE&datatype=json")
    Call<StockQuoteResponse> getStockQuote(@Query("symbol") String symbol);
}
