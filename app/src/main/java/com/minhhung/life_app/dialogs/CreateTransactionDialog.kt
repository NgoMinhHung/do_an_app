package com.minhhung.life_app.dialogs

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhhung.life_app.R
import com.minhhung.life_app.adapters.CategoriesAdapter
import com.minhhung.life_app.adapters.RemovableCategoriesAdapter
import com.minhhung.life_app.constants.Sources
import com.minhhung.life_app.constants.TransactionDialogTypes
import com.minhhung.life_app.constants.TransactionTypes
import com.minhhung.life_app.constants.Types
import com.minhhung.life_app.models.CreateTransactionParams
import com.minhhung.life_app.models.CreateTransactionResponse
import com.minhhung.life_app.models.TransactionData
import com.minhhung.life_app.services.ApiService
import com.minhhung.life_app.services.implementations.TransactionService
import com.minhhung.life_app.utils.getCurrencyFormatter
import com.minhhung.life_app.utils.hide
import com.minhhung.life_app.utils.show
import com.minhhung.life_app.widgets.LoadingDialog
import kotlinx.android.synthetic.main.dialog_create_transaction.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class CreateTransactionDialog(
    context: Context,
    private val titleId: Int,
    private val dialogTypes: Int
) : Dialog(context) {
    var onTransactionCreated: ((TransactionData) -> Unit)? = null

    lateinit var loadingDialog: LoadingDialog

    private var calendar = Calendar.getInstance()

    private var source: String = Sources.Cash

    private var type: String = TransactionTypes.Income

    private var canChooseSource = false

    private var amount = 0f

    private val adapter = RemovableCategoriesAdapter()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_create_transaction)

        init()
    }

    private fun setTypeAndSource() {
        when (dialogTypes) {
            TransactionDialogTypes.AddAtm -> {
                source = Sources.Atm
                type = TransactionTypes.Income
                hideSourceGroup()
            }
            TransactionDialogTypes.AddCash -> {
                source = Sources.Cash
                type = TransactionTypes.Income
                hideSourceGroup()
            }
            TransactionDialogTypes.AddOutcome -> {
                type = TransactionTypes.Outcome
                showSourceGroup()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun init() {
        setTypeAndSource()

        tvTitle.setText(titleId)

        rvCategories.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvCategories.adapter = adapter

        loadingDialog = LoadingDialog(context, R.string.loading)

        btnOk.setOnClickListener {
            onOkClick()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        tvDate.setOnClickListener {
            showPickDateDialog()
        }

        tvTime.setOnClickListener {
            showPickTimeDialog()
        }

        showDateTime()

        tvAmount.onCurrencyInput {
            amount = it
        }

        tvCategory.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val categoryName = tvCategory.text.toString().trim()
                if (categoryName.isNotBlank()) {
                    adapter.addCategory(categoryName)
                }
                tvCategory.text.clear()
                true
            } else {
                false
            }
        }
    }

    private fun hideSourceGroup() {
        sourceGroup.hide()
        tvSource.hide()
        canChooseSource = false
    }

    private fun showSourceGroup() {
        sourceGroup.show()
        tvSource.show()
        canChooseSource = true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showPickDateDialog() {
        DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                showDateTime()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun showDateTime() {
        tvDate.text = SimpleDateFormat("dd-MM-yyyy").format(calendar.time)
        tvTime.text = SimpleDateFormat("HH:mm").format(calendar.time)
    }

    private fun showPickTimeDialog() {
        TimePickerDialog(
            context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                showDateTime()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun onOkClick() {
        if (tvAmount.text().isBlank() || tvNote.text().isBlank()) {
            return
        }
        val note = tvNote.text()

        if (canChooseSource) {
            source = if (optionAtm.isChecked) Sources.Atm else Sources.Cash
        }

        val createTransactionParams = CreateTransactionParams(
            amount = amount,
            note = note,
            time = calendar.time,
            source = source,
            categories = adapter.getCategories(),
            transaction_type = type
        )

        createTransaction(createTransactionParams)
    }

    private fun createTransaction(createTransactionParams: CreateTransactionParams) {
        val observable = TransactionService.createTransaction(
            context,
            createTransactionParams = createTransactionParams
        )
        showLoading()
        ApiService.call(
            observable = observable,
            onSuccess = this::onCreateTransactionSuccess,
            onError = this::onError
        )
    }

    private fun onCreateTransactionSuccess(createTransactionResponse: CreateTransactionResponse) {
        hideLoading()
        dismiss()
        onTransactionCreated?.invoke(createTransactionResponse.data)
    }

    private fun onError(message: String?) {
        context.toast(message.toString())
        hideLoading()
    }

    private fun showLoading() {
        loadingDialog.show()
    }

    private fun hideLoading() {
        loadingDialog.hide()
    }

    override fun show() {
        super.show()
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}