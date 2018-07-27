package com.codebaum.mystockpricechecker.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.codebaum.mystockpricechecker.R
import com.codebaum.mystockpricechecker.inflate
import kotlinx.android.synthetic.main.row_detail.view.*

/**
 * Created on 7/26/18.
 */
class DetailRow @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        inflate(R.layout.row_detail, true)

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.DetailRow, 0, 0)

            val label = typedArray.getString(R.styleable.DetailRow_DetailRow_label)
            val value = typedArray.getString(R.styleable.DetailRow_DetailRow_value)

            tv_detail_label.text = label
            tv_detail_value.text = value

            typedArray.recycle()
        }
    }

    fun update(value: String?) {
        tv_detail_value.text = value ?: "Unknown"
    }

    fun update(value: Double?) {
        tv_detail_value.text = if (value != null) value.toString() else "--"
    }
}