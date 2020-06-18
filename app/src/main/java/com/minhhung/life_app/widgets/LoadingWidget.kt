package com.minhhung.life_app.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.minhhung.life_app.R

class LoadingWidget : LinearLayout {
    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
        View.inflate(context, R.layout.widget_loading, this)
    }
}