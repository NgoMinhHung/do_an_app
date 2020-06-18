package com.minhhung.life_app.widgets

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.minhhung.life_app.utils.getCurrencyFormatter
import kotlinx.android.synthetic.main.dialog_create_transaction.*

class HasTextChangedActionEditText : AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun onTextChanged(onTextChanged: (String) -> Unit) {
        addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged.invoke(s.toString())
            }

        })
    }

    fun onCurrencyInput(onCurrencyInput: (Float) -> Unit) {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                this@HasTextChangedActionEditText.removeTextChangedListener(this)
                val amountInString = s.toString().replace(Regex("[đ,. ]"), "")
                val amount = if (amountInString.isBlank()) 0f else amountInString.toFloat()
                val formatted = getCurrencyFormatter().format(amount)
                onCurrencyInput.invoke(amount)
                this@HasTextChangedActionEditText.setText(formatted)
                this@HasTextChangedActionEditText.setSelection(formatted.length - 2)
                this@HasTextChangedActionEditText.addTextChangedListener(this)
            }
        })
    }

    fun setCurrencyTextSelection() {
        requestFocus()
        setSelection(text().length - 2)
    }

    fun setCurrencyText(value: Float) {
        setText(getCurrencyFormatter().format(value))
    }

    fun text() = text.toString()
}