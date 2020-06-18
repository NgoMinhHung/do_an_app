package com.minhhung.life_app.services

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private var retrofit : Retrofit? = null

    private val builder = Retrofit.Builder()
        .baseUrl("https://lazy-life-api.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    private val httpClient = OkHttpClient.Builder()

    fun <C> createService(serviceClass: Class<C>) : C? {
        builder.client(httpClient.build())
        retrofit = builder.build()
        return retrofit?.create(serviceClass)
    }

}