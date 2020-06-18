package com.minhhung.life_app.models

import com.google.gson.annotations.SerializedName

data class TransactionsData(
    val user: User,
    @SerializedName("transaction_items")
    val transactions: MutableList<Transaction> = mutableListOf()
)