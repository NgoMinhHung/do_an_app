package com.minhhung.life_app.models

import java.util.Date

data class Transaction(
    val id: Int,
    val amount: Float,
    val time: Date,
    val note: String,
    val transaction_type: String,
    val source: String,
    val categories: MutableList<Category>
)