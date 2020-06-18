package com.minhhung.life_app.models

import com.google.gson.annotations.SerializedName

data class SignUpParams(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("confirmPassword")
    val confirmPassword: String
)