package com.minhhung.life_app.services.interfaces

import com.minhhung.life_app.models.GetUserResponse
import com.minhhung.life_app.models.UpdateBudgetParams
import com.minhhung.life_app.models.UpdateUserParams
import com.minhhung.life_app.models.UpdateUserResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface IUserService {

    @GET("/api/users/me")
    fun getUser(@Header("Authorization") token: String): Observable<Response<GetUserResponse>>

    @PUT("/api/users/me")
    fun updateUser(@Header("Authorization") token: String, @Body updateUserParams: UpdateUserParams): Observable<Response<UpdateUserResponse>>

    @PUT("/api/users/me/config")
    fun updateBudgetConfig(@Header("Authorization") token: String, @Body updateBudgetParams: UpdateBudgetParams): Observable<Response<GetUserResponse>>
}

