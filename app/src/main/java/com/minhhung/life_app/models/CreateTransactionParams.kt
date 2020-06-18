package com.minhhung.life_app.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class CreateTransactionParams(
    @SerializedName("amount")
    val amount: Float,
    @SerializedName("time")
    val time: Date,
    @SerializedName("note")
    val note: String,
    @SerializedName("transaction_type")
    val transaction_type: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("categories")
    val categories: MutableList<String> = mutableListOf()
)