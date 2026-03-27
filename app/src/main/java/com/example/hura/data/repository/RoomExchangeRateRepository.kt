package com.example.hura.data.repository

import com.example.hura.data.local.dao.ExchangeRateDao
import com.example.hura.data.local.entity.ExchangeRateEntity
import com.example.hura.data.remote.exchangerate.api.ExchangeRateApiClient
import com.example.hura.domain.currency.FallbackExchangeRates
import com.example.hura.domain.repository.ExchangeRateRepository
import java.time.Instant

class RoomExchangeRateRepository(
    private val dao: ExchangeRateDao
) : ExchangeRateRepository {

    override suspend fun insertRates(rates: List<ExchangeRateEntity>) {
        dao.insertAll(rates)
    }

    override suspend fun getLatestRate(currency: String): ExchangeRateEntity {
        val rateFromDb = dao.getLatestRates().firstOrNull { it.currency == currency.uppercase() }

        val finalRate = rateFromDb?.rateToEur
            ?: FallbackExchangeRates.getRate(currency)
            ?: throw IllegalArgumentException("No rate available for currency: $currency")

        return ExchangeRateEntity(
            currency = currency.uppercase(),
            rateToEur = finalRate.toString(),
            timestamp = rateFromDb?.timestamp ?: Instant.now().toEpochMilli()
        )
    }

    override suspend fun getLatestRates(): Map<String, ExchangeRateEntity> {
        val ratesFromDb = dao.getLatestRates()
        val ratesMap = ratesFromDb.associateBy { it.currency.uppercase() }.toMutableMap()

        FallbackExchangeRates.eurRates.forEach { (currency, fallbackRate) ->
            val upperCurrency = currency.uppercase()
            if (!ratesMap.containsKey(upperCurrency)) {
                ratesMap[upperCurrency] = ExchangeRateEntity(
                    currency = upperCurrency,
                    rateToEur = fallbackRate.toString(),
                    timestamp = Instant.now().toEpochMilli()
                )
            }
        }

        return ratesMap
    }

    override suspend fun refreshExchangeRates() {
        val response = ExchangeRateApiClient.service.getLatestRates()
        if (response.result != "success") return
        val timestamp = response.time_last_update_unix
        val entities = response.rates.map { (currency, rate) ->
            ExchangeRateEntity(
                currency = currency,
                rateToEur = String.format("%.2f", rate),
                timestamp = timestamp
            )
        }
        dao.insertAll(entities)
    }
}