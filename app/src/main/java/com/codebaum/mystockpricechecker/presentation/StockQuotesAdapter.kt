package com.codebaum.mystockpricechecker.presentation

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.codebaum.mystockpricechecker.R
import com.codebaum.mystockpricechecker.data.SingleStockQuoteResult
import com.codebaum.mystockpricechecker.inflate
import com.codebaum.mystockpricechecker.presentation.StockQuotesAdapter.ViewHolder
import kotlinx.android.synthetic.main.item_quote.view.*

/**
 * Created on 7/26/18.
 */
class StockQuotesAdapter(val callback: QuotePriceClickListener) : RecyclerView.Adapter<ViewHolder>() {

    interface QuotePriceClickListener {
        fun onClicked(position: Int)
    }

    private val items: ArrayList<SingleStockQuoteResult> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.item_quote, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items[position]
        val itemView = holder.itemView

        itemView.tv_quote_ticker_symbol.text = item.symbol
        itemView.tv_quote_price.text = item.latestPrice.toString()

        item.latestPrice?.let { latest ->
            item.previousClose?.let { previousClose ->
                var color = itemView.tv_quote_price.currentTextColor
                if (latest > previousClose) {
                    color = ContextCompat.getColor(itemView.context, R.color.price_increase_indicator)
                } else if (previousClose > latest) {
                    color = ContextCompat.getColor(itemView.context, R.color.price_decrease_indicator)
                }
                itemView.tv_quote_price.setTextColor(color)
            }
        }

        holder.itemView.tv_quote_price.setOnClickListener { callback.onClicked(position) }
    }

    fun update(newItems: List<SingleStockQuoteResult>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: SingleStockQuoteResult) {
            itemView.tv_quote_ticker_symbol.text = item.symbol
            itemView.tv_quote_price.text = item.latestPrice.toString()

            item.latestPrice?.let { latest ->
                item.previousClose?.let { previousClose ->
                    var color = itemView.tv_quote_price.currentTextColor
                    if (latest > previousClose) {
                        color = ContextCompat.getColor(itemView.context, R.color.price_increase_indicator)
                    } else if (previousClose > latest) {
                        color = ContextCompat.getColor(itemView.context, R.color.price_decrease_indicator)
                    }
                    itemView.tv_quote_price.setTextColor(color)
                }
            }
        }
    }
}