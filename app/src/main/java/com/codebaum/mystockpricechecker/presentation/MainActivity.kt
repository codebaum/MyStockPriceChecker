package com.codebaum.mystockpricechecker.presentation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import com.codebaum.mystockpricechecker.App
import com.codebaum.mystockpricechecker.R
import com.codebaum.mystockpricechecker.data.SingleStockQuoteResult
import com.codebaum.mystockpricechecker.hideKeyboard
import com.codebaum.mystockpricechecker.toastLong
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var filterType = FilterOptions.ALL

    private val history = arrayListOf<SingleStockQuoteResult>()

    private lateinit var prefs: SharedPreferences

    private val stockQuotesAdapter = StockQuotesAdapter(object : StockQuotesAdapter.QuotePriceClickListener {
        override fun onClicked(position: Int) {
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("quote", getFilteredItems()[position])
            startActivity(intent)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_stock_entry.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                processSymbolRequest(v)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        btn_stock_entry.setOnClickListener {
            processSymbolRequest(it)
        }

        rv_search_history.layoutManager = LinearLayoutManager(this)
        rv_search_history.adapter = stockQuotesAdapter

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
    }

    private fun processSymbolRequest(v: View) {
        filterType = FilterOptions.ALL
        v.hideKeyboard()
        getSingleStockQuote()
        et_stock_entry.text.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.filter_winners -> filterType = FilterOptions.WINNERS
            R.id.filter_losers -> filterType = FilterOptions.LOSERS
            R.id.filter_none -> filterType = FilterOptions.ALL
            R.id.action_clear_data -> clearData()
        }

        updateHistoryDisplay()

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()

        val saved = prefs.getString("json", null)
        saved?.let {
            // do something
            history.clear()
            val items = App.gson.fromJson(it, Array<SingleStockQuoteResult>::class.java).toList()
            history.addAll(items)

            val savedFilter = prefs.getString("filter", "ALL")
            filterType = FilterOptions.valueOf(savedFilter)

            updateHistoryDisplay()
        }
    }

    override fun onStop() {
        super.onStop()

        val filterToSave = filterType.name
        prefs.edit().putString("json", App.gson.toJson(history)).putString("filter", filterToSave).apply()
    }

    private fun getSingleStockQuote() {
        val enteredTickerSymbol = et_stock_entry.text.toString()
        App.stockService.getSingleStockQuote(enteredTickerSymbol).enqueue(object : Callback<SingleStockQuoteResult> {

            override fun onFailure(call: Call<SingleStockQuoteResult>?, t: Throwable?) {
                toastLong("Network exception occurred.")
            }

            override fun onResponse(call: Call<SingleStockQuoteResult>?, response: Response<SingleStockQuoteResult>?) {
                response?.let {
                    if (it.isSuccessful) {
                        it.body()?.let { quote ->
                            addQuoteToHistory(quote)
                        }
                    } else {
                        toastLong("Unable to retrieve price.")
                    }
                }
            }
        })
    }

    private fun addQuoteToHistory(quote: SingleStockQuoteResult) {

        var indexToRemove = -1
        val lastIndex = history.size - 1
        for (index in 0..lastIndex) {
            if (history[index].symbol == quote.symbol) {
                indexToRemove = index
                break
            }
        }

        if (indexToRemove >= 0) {
            history.removeAt(indexToRemove)
        }

        history.add(0, quote)
        updateHistoryDisplay()
    }

    private fun updateHistoryDisplay() {
        stockQuotesAdapter.update(history.filter { it.change?.let { it < 0 } ?: false })

        val filteredItems = getFilteredItems()

        stockQuotesAdapter.update(filteredItems)
        if (filteredItems.isNotEmpty()) {
            tv_label_no_history.visibility = View.GONE
            rv_search_history.visibility = View.VISIBLE
        }
    }

    private fun getFilteredItems(): List<SingleStockQuoteResult> {
        return when (filterType) {
            FilterOptions.WINNERS -> history.filter { it.change?.let { it > 0 } ?: false }
            FilterOptions.LOSERS -> history.filter { it.change?.let { it < 0 } ?: false }
            FilterOptions.ALL -> history
        }
    }

    private fun clearData() {
        prefs.edit().remove("json").apply()
        history.clear()
        updateHistoryDisplay()
    }

    enum class FilterOptions {
        WINNERS,
        LOSERS,
        ALL
    }
}
