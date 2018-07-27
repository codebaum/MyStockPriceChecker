package com.codebaum.mystockpricechecker.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created on 7/26/18.
 */
interface StockService {

    @GET("stock/{tickerSymbol}/quote")
    fun getSingleStockQuote(
            @Path("tickerSymbol") tickerSymbol: String
    ): Call<SingleStockQuoteResult>

    companion object Factory {
        fun create(): StockService {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.iextrading.com/1.0/")
                    .build()

            return retrofit.create(StockService::class.java)
        }
    }
}