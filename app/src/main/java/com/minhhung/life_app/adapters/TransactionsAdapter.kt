package com.minhhung.life_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minhhung.life_app.R
import com.minhhung.life_app.adapters.holders.TransactionViewHolder
import com.minhhung.life_app.constants.TransactionTypes
import com.minhhung.life_app.models.Transaction

class TransactionsAdapter(private val items: MutableList<Transaction> = mutableListOf()) : RecyclerView.Adapter<TransactionViewHolder>() {

    fun setTransactions(transactions: MutableList<Transaction>) {
        items.clear()
        items.addAll(transactions.filter { it.transaction_type == TransactionTypes.Outcome }.sortedByDescending { it.time })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.item = items[position]
    }

}