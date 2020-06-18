package com.minhhung.life_app.utils

import java.text.DecimalFormat

fun String?.isNotNullOrEmpty() = this?.isNotBlank() ?: false

fun getCurrencyFormatter() = DecimalFormat("#,### đ")