package com.minhhung.life_app.widgets

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.minhhung.life_app.R
import kotlinx.android.synthetic.main.dialog_loading.*

class LoadingDialog(context: Context, @StringRes private val messageId: Int) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)

        tvMessage.setText(messageId)
    }

    override fun show() {
        super.show()
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

}