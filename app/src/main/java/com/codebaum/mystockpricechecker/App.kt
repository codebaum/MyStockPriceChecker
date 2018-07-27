package com.codebaum.mystockpricechecker

import android.app.Application
import com.codebaum.mystockpricechecker.data.StockService
import com.google.gson.Gson

/**
 * Created on 7/26/18.
 */
class App : Application() {

    companion object {
        val stockService = StockService.create()
        val gson = Gson()
    }

}