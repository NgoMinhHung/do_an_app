package com.minhhung.life_app.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.minhhung.life_app.R
import com.minhhung.life_app.adapters.TransactionsAdapter
import com.minhhung.life_app.constants.TransactionDialogTypes
import com.minhhung.life_app.constants.Types
import com.minhhung.life_app.dialogs.CreateTransactionDialog
import com.minhhung.life_app.models.Budget
import com.minhhung.life_app.models.GetTransactionsResponse
import com.minhhung.life_app.models.Transaction
import com.minhhung.life_app.services.ApiService
import com.minhhung.life_app.services.implementations.TransactionService
import com.minhhung.life_app.utils.getCurrencyFormatter
import com.minhhung.life_app.utils.hide
import com.minhhung.life_app.utils.show
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.DecimalFormat
import java.util.*

class HomeFragment : Fragment() {
    private val adapter = TransactionsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        getTransactions()
    }

    private fun init() {
        btnAddAtm.setOnClickListener {
            showAddAtmDialog()
        }

        btnAddCash.setOnClickListener {
            showAddCashDialog()
        }

        btnAddOutcome.setOnClickListener {
            showAddOutcomeDialog()
        }

        refresh.setOnRefreshListener {
            refresh()
        }

        rvTransactions.adapter = adapter
    }

    private fun showAddOutcomeDialog() {
        CreateTransactionDialog(context!!, R.string.lable_outcome, TransactionDialogTypes.AddOutcome).apply {
            onTransactionCreated = {
                this@HomeFragment.refresh()
            }
            show()
        }
    }

    private fun refresh() {
        refresh.isRefreshing = false
        getTransactions()
    }

    private fun showAddAtmDialog() {
        CreateTransactionDialog(context!!, R.string.label_atm, TransactionDialogTypes.AddAtm).apply {
            onTransactionCreated = {
                this@HomeFragment.refresh()
            }
            show()
        }
    }

    private fun showAddCashDialog() {
        CreateTransactionDialog(context!!, R.string.label_cash, TransactionDialogTypes.AddCash).apply {
            onTransactionCreated = {
                this@HomeFragment.refresh()
            }
            show()
        }
    }

    private fun getTransactions() {
        showLoading()
        val observable = TransactionService.getTransactions(context!!, Calendar.getInstance().time, Types.ByWeek)
        ApiService.call(
            observable = observable,
            onSuccess = ::onGetTransactionsSuccess,
            onError = ::onError
        )
    }

    private fun onGetTransactionsSuccess(getTransactionsResponse: GetTransactionsResponse) {
        displayBudget(getTransactionsResponse.data.user.budget)
        displayTransactions(getTransactionsResponse.data.transactions)
        hideLoading()
    }

    private fun onError(string: String?) {
        hideLoading()
    }

    private fun showLoading() {
        content?.hide()
        loadingView?.show()
    }

    private fun hideLoading() {
        content?.show()
        loadingView?.hide()
    }

    @SuppressLint("SetTextI18n")
    private fun displayBudget(budget: Budget) {
        tvAtm.text = getCurrencyFormatter().format(budget.atm)
        tvCash.text = getCurrencyFormatter().format(budget.cash)
        tvTotal.text = getCurrencyFormatter().format(budget.total)
        moneyChart.setValues(budget.atm, budget.cash)
    }

    private fun displayTransactions(transactions: MutableList<Transaction>) {
        adapter.setTransactions(transactions)
    }
}