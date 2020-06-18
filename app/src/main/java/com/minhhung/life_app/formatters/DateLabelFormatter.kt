package com.minhhung.life_app.formatters

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class DateLabelFormatter(private val labels: MutableList<String>) : ValueFormatter() {
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return value.toString()
    }
}