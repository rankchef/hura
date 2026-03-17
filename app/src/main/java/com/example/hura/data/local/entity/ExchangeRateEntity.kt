package com.example.hura.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.Instant

@Entity(
    tableName = "exchange_rates",
    indices = [Index(value = ["currency", "timestamp"])]
)
data class ExchangeRateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val currency: String,
    val rateToEur: String,
    val timestamp: Long
)