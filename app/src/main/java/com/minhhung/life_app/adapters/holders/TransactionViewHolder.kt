package com.minhhung.life_app.adapters.holders

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.minhhung.life_app.R
import com.minhhung.life_app.adapters.CategoriesAdapter
import com.minhhung.life_app.constants.Sources
import com.minhhung.life_app.models.Transaction
import com.minhhung.life_app.utils.getCurrencyFormatter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_transaction.*
import java.text.SimpleDateFormat

class TransactionViewHolder(override val containerView: View?) :
    RecyclerView.ViewHolder(containerView!!), LayoutContainer {
    private val adapter = CategoriesAdapter()

    var item: Transaction? = null
        set(value) {
            field = value
            display()
        }

    init {
        rvCategories.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        rvCategories.adapter = adapter
    }

    @SuppressLint("SimpleDateFormat")
    private fun display() {
        item?.apply {
            tvTitle.text = note
            tvAmount.text = getCurrencyFormatter().format(amount)
            tvDate.text = SimpleDateFormat("HH:mm dd-MM-yyyy").format(time)
            imgSource.setImageResource(if (source == Sources.Atm) R.drawable.ic_credit_card else R.drawable.ic_money)
            adapter.setCategories(categories)
        }
    }
}