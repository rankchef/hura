package com.example.hura.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    indices = [Index(value = ["notificationKey"], unique = true)]
)

data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: String, //store BigDecimal as String
    val currency: String,
    val merchant: String,
    val timestamp: Long,
    val notificationKey: String,
    val type: String //store enum as String
)
