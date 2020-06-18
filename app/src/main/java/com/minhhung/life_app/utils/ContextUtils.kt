package com.minhhung.life_app.utils

import android.content.Context
import android.content.SharedPreferences
import com.minhhung.life_app.models.SignInResponse
import com.minhhung.life_app.utils.Constants.PreferenceKeys

fun Context.saveAuthInformation(signInResponse: SignInResponse) {
    authPreferences().edit().apply {
        putString(PreferenceKeys.Token, signInResponse.data.token)
        putInt(PreferenceKeys.UserId, signInResponse.data.user.id)
        apply()
    }
}

fun Context.deleteAuthInformation() {
    authPreferences().edit().apply() {
        remove(PreferenceKeys.Token)
        remove(PreferenceKeys.UserId)
        apply()
    }
}

fun Context.getToken(): String? {
    return authPreferences().getString(PreferenceKeys.Token, "")
}

fun Context.getUserId(): Int {
    return authPreferences().getInt(PreferenceKeys.UserId, 0)
}

fun Context.authPreferences(): SharedPreferences =
    this.getSharedPreferences(PreferenceKeys.AuthPreferenceName, Context.MODE_PRIVATE)
