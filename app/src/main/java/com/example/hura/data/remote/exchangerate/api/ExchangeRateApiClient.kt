package com.example.hura.data.remote.exchangerate.api

import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
object ExchangeRateApiClient {

    private const val BASE_URL = "https://open.er-api.com/"

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory()) // important for Kotlin data classes
            .build()
    }

    val service: ExchangeRateApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ExchangeRateApiService::class.java)
    }
}