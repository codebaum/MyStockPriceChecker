package com.codebaum.mystockpricechecker

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

/**
 * Created on 7/26/18.
 */
fun Context.toastLong(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun ViewGroup.inflate(@LayoutRes layoutResId: Int, attachToRoot: Boolean): View = LayoutInflater.from(this.context).inflate(layoutResId, this, attachToRoot)