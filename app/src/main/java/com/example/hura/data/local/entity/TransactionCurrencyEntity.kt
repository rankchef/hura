package com.example.hura.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "transaction_currencies",
    indices = [Index(value = ["transactionId", "currency"], unique = true)]
)
data class TransactionCurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val transactionId: String, // FK to your transaction table
    val currency: String,      // ISO 4217 code, e.g., USD, EUR
    val price: String,         // Converted price in that currency (store as String for BigDecimal)
    val timestamp: Long     // When this row was created (usually transaction creation time)
)