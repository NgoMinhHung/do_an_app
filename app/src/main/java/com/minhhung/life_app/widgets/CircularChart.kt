package com.minhhung.life_app.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircularChart : View {

    private var creditBalanceRatio: Float = 0f

    private var cashBalanceRatio: Float = 0f

    fun setValues(creditBalance: Float, cashBalance: Float) {
        val total = creditBalance + cashBalance
        if (total > 0) {
            creditBalanceRatio = creditBalance * 100f / total
            cashBalanceRatio = 100f - creditBalanceRatio
            invalidate()
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private val bluePaint = Paint().apply {
        isAntiAlias = true
        color = Color.rgb(33, 150, 243)
        style = Paint.Style.FILL
    }

    private val greenPaint = Paint().apply {
        isAntiAlias = true
        color = Color.rgb(4, 206, 11)
        style = Paint.Style.FILL
    }


    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawArc(0f, 0f, width.toFloat(), width.toFloat(), 0f, creditBalanceRatio * 3.6f, true, bluePaint)
            drawArc(0f, 0f, width.toFloat(), width.toFloat(), creditBalanceRatio * 3.6f, cashBalanceRatio * 3.6f, true, greenPaint)
        }
    }

}