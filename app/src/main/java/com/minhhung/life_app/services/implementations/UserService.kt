package com.minhhung.life_app.services.implementations

import android.content.Context
import com.minhhung.life_app.models.UpdateBudgetParams
import com.minhhung.life_app.models.UpdateUserParams
import com.minhhung.life_app.services.ApiClient
import com.minhhung.life_app.services.interfaces.IUserService
import com.minhhung.life_app.utils.getToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object UserService {
    fun getUser(context: Context) = ApiClient
        .createService(IUserService::class.java)!!
        .getUser(context.getToken()!!)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())!!

    fun updateUser(context: Context, updateUserParams: UpdateUserParams) = ApiClient
        .createService(IUserService::class.java)!!
        .updateUser(context.getToken()!!, updateUserParams)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())!!

    fun updateBudgetConfig(context: Context, updateBudgetParams: UpdateBudgetParams) = ApiClient
        .createService(IUserService::class.java)!!
        .updateBudgetConfig(context.getToken()!!, updateBudgetParams)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())!!
}