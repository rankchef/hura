package com.example.hura.data.remote.api

interface ExchangeRateApiService {

    @GET("v6/latest/EUR")
    suspend fun getLatestRates(): ExchangeRateResponse
}