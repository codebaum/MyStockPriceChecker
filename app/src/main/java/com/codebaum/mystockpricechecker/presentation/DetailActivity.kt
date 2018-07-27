package com.codebaum.mystockpricechecker.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.codebaum.mystockpricechecker.R
import com.codebaum.mystockpricechecker.data.SingleStockQuoteResult
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val quote : SingleStockQuoteResult = intent.extras["quote"] as SingleStockQuoteResult

        supportActionBar?.title = quote.companyName

        quote.run {
            detail_ticker_symbol.update(symbol)
            detail_exchange.update(primaryExchange)
            detail_sector.update(sector)

            detail_latest_price.update(latestPrice)
            detail_opening_price.update(open)
            detail_closing_price.update(close)
            detail_previous_closing_price.update(previousClose)

            val changeValue = "$change%"
            detail_percentage_change.update(changeValue)
        }
    }
}
