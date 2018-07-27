package com.codebaum.mystockpricechecker.data

import java.io.Serializable

data class SingleStockQuoteResult(
        val symbol: String? = null,
        val companyName: String? = null,
        val primaryExchange: String? = null,
        val sector: String? = null,
        val open: Double? = null,
        val close: Double? = null,
        val latestPrice: Double? = null,
        val previousClose: Double? = null,
        val change: Double? = null
) : Serializable
