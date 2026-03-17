package com.example.hura.data.repository

import com.example.hura.data.local.entity.ExchangeRateEntity
import java.time.Instant

interface ExchangeRateRepository {
    suspend fun insertRates(rates: List<ExchangeRateEntity>)
    suspend fun getLatestRate(currency: String): ExchangeRateEntity
    //k: Currency ISO4217 code
    suspend fun getLatestRates(): Map<String, ExchangeRateEntity>

    suspend fun refreshExchangeRates()
}