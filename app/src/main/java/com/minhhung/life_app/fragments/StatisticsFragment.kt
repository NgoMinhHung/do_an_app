package com.minhhung.life_app.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.minhhung.life_app.R
import com.minhhung.life_app.constants.TransactionTypes
import com.minhhung.life_app.constants.Types
import com.minhhung.life_app.formatters.DateLabelFormatter
import com.minhhung.life_app.models.GetTransactionsResponse
import com.minhhung.life_app.models.Transaction
import com.minhhung.life_app.services.ApiService
import com.minhhung.life_app.services.implementations.TransactionService
import com.minhhung.life_app.utils.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class StatisticsFragment : Fragment() {
    object StatisticModes {
        const val ByWeek = 0
        const val ByMonth = 1
    }

    private var mode = StatisticModes.ByWeek

    private var type = Types.ByWeek

    private val current = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    @SuppressLint("SimpleDateFormat")
    private fun init() {
        ArrayAdapter.createFromResource(
            context!!,
            R.array.types,
            R.layout.item_spinner
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTypes.adapter = adapter
            spinnerTypes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    mode = position
                    refresh()
                }

            }
        }

        btnPrevious.setOnClickListener {
            changeTime(-1)
        }

        btnNext.setOnClickListener {
            changeTime(1)
        }

        refresh.setOnRefreshListener {
            refresh()
        }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun refresh() {
        refresh.isRefreshing = false
        when (mode) {
            StatisticModes.ByWeek -> {
                type = Types.ByWeek
                tvTime.text = "Tuần ${weekAndYearFormat.format(current.time)}"
                Calendar.getInstance().apply {
                    time = current.time
                    set(Calendar.DAY_OF_WEEK, 2)
                    val beginOfWeek = dateAndMonthFormat.format(time)
                    add(Calendar.DATE,6)
                    val endOfWeek = dateAndMonthFormat.format(time)
                    tvBonusTime.text = "$beginOfWeek - $endOfWeek"
                }
            }
            StatisticModes.ByMonth -> {
                type = Types.ByMonth
                tvTime.text = "Tháng ${monthAndYearFormat.format(current.time)}"

                Calendar.getInstance().apply {
                    time = current.time
                    set(Calendar.DATE, 1)
                    val beginOfMonth = dateAndMonthFormat.format(time)
                    add(Calendar.MONTH,1)
                    add(Calendar.DATE , -1)
                    val endOfMonth = dateAndMonthFormat.format(time)
                    tvBonusTime.text = "$beginOfMonth - $endOfMonth"
                }
            }
            else -> {
                type = Types.ByDate
            }
        }

        getData()
    }

    private fun changeTime(timeToChange: Int = 0) {
        when (mode) {
            StatisticModes.ByWeek -> {
                current.add(Calendar.WEEK_OF_YEAR, timeToChange)
                refresh()
            }
            StatisticModes.ByMonth -> {
                current.add(Calendar.MONTH, timeToChange)
                refresh()
            }
        }
    }

    private fun getData() {
        val observable = TransactionService.getTransactions(context!!, current.time, type)
        showLoading()
        ApiService.call(
            observable = observable,
            onSuccess = this::onGetTransactionsSuccess,
            onError = this::onError
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun showBarChart(transactions: MutableList<Transaction>) {
        val times = mutableListOf<String>()
        val incomeEntries = mutableListOf<BarEntry>()
        val outcomeEntries = mutableListOf<BarEntry>()

        transactions.sortBy { it.time }

        var index = 0
        transactions.groupBy {
            val calendar = Calendar.getInstance()
            calendar.time = it.time
            calendar.set(Calendar.HOUR, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.time
        }.forEach { entry ->
            incomeEntries.add(
                BarEntry(
                    index.toFloat(),
                    entry.value.sumByDouble {
                        if (it.transaction_type == TransactionTypes.Income) {
                            it.amount.toDouble()
                        } else {
                            0.toDouble()
                        }
                    }.toFloat()
                )
            )

            outcomeEntries.add(
                BarEntry(
                    index.toFloat(),
                    entry.value.sumByDouble {
                        if (it.transaction_type == TransactionTypes.Outcome) {
                            it.amount.toDouble()
                        } else {
                            0.toDouble()
                        }
                    }.toFloat()
                )
            )
            times.add(SimpleDateFormat("dd/MM").format(entry.key))
            index++
        }


        val incomeDataSet = BarDataSet(incomeEntries, "Thu nhập")
        val outcomeDataSet = BarDataSet(outcomeEntries, "Chi tiêu")
        incomeDataSet.color = Color.BLUE
        incomeDataSet.valueTextColor = Color.BLUE

        incomeDataSet.color = Color.GREEN
        incomeDataSet.valueTextColor = Color.GREEN

        val barData = BarData(incomeDataSet, outcomeDataSet)
        barChart.animateY(1000)
        barChart.xAxis.valueFormatter = DateLabelFormatter(times)
        barChart.data = barData
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.xAxis.isGranularityEnabled = true
        barChart.xAxis.setCenterAxisLabels(true)

        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false

        barChart.barData?.apply {
            barWidth = 5f
        }

        val groupSpace = 1f
        val barSpace = 0f

        barChart.xAxis.apply {
            axisMinimum = 0f
            axisMaximum = barChart.barData.getGroupWidth(groupSpace, barSpace) * times.size
        }

        barChart.groupBars(0f, groupSpace, barSpace)
        barChart.invalidate()

        barChart.axisLeft.apply {
            axisMinimum = 0f
            isEnabled = false
        }

        barChart.axisRight.apply {
            axisMinimum = 0f
            isEnabled = false
        }
    }

    private fun onGetTransactionsSuccess(getTransactionsResponse: GetTransactionsResponse) {
        hideLoading()
        showBarChart(getTransactionsResponse.data.transactions)
    }

    private fun onError(message: String?) {
        hideLoading()
        context!!.toast(message.toString())
    }

    private fun showLoading() {
        spinnerTypes.isEnabled = false
        btnPrevious.isEnabled = false
        btnNext.isEnabled = false
        barChart.hide()
        loadingView.show()
    }

    private fun hideLoading() {
        spinnerTypes.isEnabled = true
        btnPrevious.isEnabled = true
        btnNext.isEnabled = true
        barChart.show()
        loadingView.hide()
    }
}