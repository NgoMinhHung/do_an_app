package com.minhhung.life_app.services

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.minhhung.life_app.models.ErrorModel
import io.reactivex.Observable
import retrofit2.Response

object ApiService {

    @SuppressLint("CheckResult")
    fun <T> call(
        observable: Observable<Response<T>>,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((String?) -> Unit)? = null
    ) {
        observable.subscribe({
            if (it.isSuccessful) {
                onSuccess?.invoke(it.body()!!)
            } else {
                val errorModel = Gson().fromJson(it.errorBody()?.string(), ErrorModel::class.java)

                onError?.invoke(errorModel.message)
            }
        }, {
            onError?.invoke(it.message)
        })
    }

}