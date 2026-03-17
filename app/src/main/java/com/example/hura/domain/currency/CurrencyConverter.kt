package com.example.hura.domain.currency

import java.math.BigDecimal
import java.math.RoundingMode

object CurrencyConverter {

    fun convert(
        amount: BigDecimal,
        fromCurrency: String,
        toCurrency: String,
        eurRates: Map<String, BigDecimal>
    ): BigDecimal {

        val from = fromCurrency.uppercase()
        val to = toCurrency.uppercase()

        if (from == to) return amount

        val fromRate = eurRates[from]
            ?: throw IllegalArgumentException("Unsupported currency: $from")

        val toRate = eurRates[to]
            ?: throw IllegalArgumentException("Unsupported currency: $to")

        // Convert to EUR first
        val amountInEur = amount.divide(fromRate, 8, RoundingMode.HALF_UP)

        // Then EUR → target
        return amountInEur.multiply(toRate)
            .setScale(2, RoundingMode.HALF_UP)
    }
}