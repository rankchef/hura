package com.example.hura.data.remote.exchangerate.api

import com.example.hura.data.remote.exchangerate.model.ExchangeRateResponse
import retrofit2.http.GET

interface ExchangeRateApiService {

    @GET("v6/latest/EUR")
    suspend fun getLatestRates(): ExchangeRateResponse
}