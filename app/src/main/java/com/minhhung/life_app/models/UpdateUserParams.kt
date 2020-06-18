package com.minhhung.life_app.models

import com.google.gson.annotations.SerializedName

data class UpdateUserParams(
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("gender")
    val gender: Boolean,
    @SerializedName("image_url")
    val imageUrl: String? = null
)