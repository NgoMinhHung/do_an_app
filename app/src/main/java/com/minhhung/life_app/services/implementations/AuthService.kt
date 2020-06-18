package com.minhhung.life_app.services.implementations

import com.minhhung.life_app.models.SignInParams
import com.minhhung.life_app.models.SignUpParams
import com.minhhung.life_app.services.ApiClient
import com.minhhung.life_app.services.interfaces.IAuthService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object AuthService {
    fun signIn(signInParams: SignInParams) = ApiClient
        .createService(IAuthService::class.java)!!
        .signIn(signInParams)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())!!

    fun signUp(signUpParams: SignUpParams) = ApiClient
        .createService(IAuthService::class.java)!!
        .signUp(signUpParams)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())!!
}