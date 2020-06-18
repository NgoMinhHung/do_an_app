package com.minhhung.life_app.models

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val name: String,
    val address: String?,
    val gender: Boolean = false,
    val phone: String?,
    @SerializedName("image_url") val imageUrl: String?,
    val budget: Budget
)