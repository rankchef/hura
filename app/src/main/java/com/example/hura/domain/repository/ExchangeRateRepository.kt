package com.example.hura.domain.repository

import com.example.hura.data.local.entity.ExchangeRateEntity

interface ExchangeRateRepository {
    suspend fun insertRates(rates: List<ExchangeRateEntity>)
    suspend fun getLatestRate(currency: String): ExchangeRateEntity
    //k: Currency ISO4217 code
    suspend fun getLatestRates(): Map<String, ExchangeRateEntity>

    suspend fun refreshExchangeRates()
}