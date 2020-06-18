package com.minhhung.life_app.services.interfaces

import com.minhhung.life_app.models.CreateTransactionParams
import com.minhhung.life_app.models.CreateTransactionResponse
import com.minhhung.life_app.models.GetTransactionsResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface ITransactionService {

    @POST("/api/users/me/transaction_items")
    fun createTransaction(@Header("Authorization") token: String, @Body createTransactionParams: CreateTransactionParams): Observable<Response<CreateTransactionResponse>>

    @GET("/api/users/me/transaction_items")
    fun getTransactions(@Header("Authorization") token: String, @Query("date") date: Date, @Query("type") type: String): Observable<Response<GetTransactionsResponse>>
}