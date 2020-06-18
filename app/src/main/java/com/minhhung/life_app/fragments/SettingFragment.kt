package com.minhhung.life_app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.minhhung.life_app.R
import com.minhhung.life_app.models.Budget
import com.minhhung.life_app.models.GetUserResponse
import com.minhhung.life_app.models.UpdateBudgetParams
import com.minhhung.life_app.services.ApiService
import com.minhhung.life_app.services.implementations.UserService
import com.minhhung.life_app.utils.hide
import com.minhhung.life_app.utils.show
import kotlinx.android.synthetic.main.fragment_setting.*
import org.jetbrains.anko.toast

class SettingFragment : Fragment() {

    private var hasLimitedOutcomePerWeek = false

    private var hasLimitedOutcomePerMonth = false

    private var limitedOutcomePerWeek = 0f

    private var limitedOutcomePerMonth = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        getData()

        switchLimitedOutcomePerWeek.setOnCheckedChangeListener { _, isChecked ->
            hasLimitedOutcomePerWeek = isChecked
            updateViews()
        }

        switchLimitedOutcomePerMonth.setOnCheckedChangeListener { _, isChecked ->
            hasLimitedOutcomePerMonth = isChecked
            updateViews()
        }

        edtLimitedOutcomePerWeek.onCurrencyInput {
            limitedOutcomePerWeek = it
        }

        edtLimitedOutcomePerMonth.onCurrencyInput {
            limitedOutcomePerMonth = it
        }

        btnSaveConfig.setOnClickListener {
            updateBudgetConfig()
        }
    }

    private fun updateViews() {
        switchLimitedOutcomePerWeek.isChecked = hasLimitedOutcomePerWeek
        switchLimitedOutcomePerMonth.isChecked = hasLimitedOutcomePerMonth
        edtLimitedOutcomePerWeek.setCurrencyText(limitedOutcomePerWeek)
        edtLimitedOutcomePerMonth.setCurrencyText(limitedOutcomePerMonth)

        if (hasLimitedOutcomePerWeek) {
            edtLimitedOutcomePerWeek.show()
        } else {
            edtLimitedOutcomePerWeek.hide()
        }

        if (hasLimitedOutcomePerMonth) {
            edtLimitedOutcomePerMonth.show()
        } else {
            edtLimitedOutcomePerMonth.hide()
        }
    }

    private fun getData() {
        showLoading()
        val observable = UserService.getUser(context!!)
        ApiService.call(
            observable = observable,
            onSuccess = ::onGetUserSuccess,
            onError = ::onError
        )
    }

    private fun updateBudgetConfig() {
        val updateBudgetParams = UpdateBudgetParams(
            hasLimitedOutcomePerWeek = hasLimitedOutcomePerWeek,
            limitedOutcomePerWeek = limitedOutcomePerWeek,
            hasLimitedOutcomePerMonth = hasLimitedOutcomePerMonth,
            limitedOutcomePerMonth = limitedOutcomePerMonth
        )

        val observable = UserService.updateBudgetConfig(context!!, updateBudgetParams)
        showLoading()
        ApiService.call(
            observable = observable,
            onSuccess = ::onGetUserSuccess,
            onError = ::onError
        )
    }

    private fun setData(budget: Budget) {
        budget.apply {
            this@SettingFragment.hasLimitedOutcomePerWeek = hasLimitedOutcomePerWeek
            this@SettingFragment.hasLimitedOutcomePerMonth = hasLimitedOutcomePerMonth
            this@SettingFragment.limitedOutcomePerWeek = limitedOutcomePerWeek
            this@SettingFragment.limitedOutcomePerMonth = limitedOutcomePerMonth
        }

        updateViews()
    }

    private fun onGetUserSuccess(getUserResponse: GetUserResponse) {
        hideLoading()
        setData(getUserResponse.data.user.budget)
    }

    private fun onError(message: String?) {
        hideLoading()
        context!!.toast(message.toString())
    }

    private fun showLoading() {
        loadingView.show()
        content.hide()
    }

    private fun hideLoading() {
        loadingView.hide()
        content.show()
    }
}