package com.example.hura.data.remote.model

data class ExchangeRateResponse(
    val result: String,
    val time_last_update_unix: Long,
    val rates: Map<String, Double>
)
