package com.minhhung.life_app.services.interfaces

import com.minhhung.life_app.models.*
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface IAuthService {

    @POST("api/auth/sign_in")
    fun signIn(@Body signInParams: SignInParams): Observable<Response<SignInResponse>>

    @POST("api/auth/sign_up")
    fun signUp(@Body signUpParams: SignUpParams): Observable<Response<SignUpResponse>>

}