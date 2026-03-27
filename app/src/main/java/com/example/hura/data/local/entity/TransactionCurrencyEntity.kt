package com.example.hura.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaction_currencies",
    indices = [Index(value = ["transactionId", "currency"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class, // Point to your main Transaction table
            parentColumns = ["id"],            // The column name in the Transaction table
            childColumns = ["transactionId"],   // The column name in THIS table
            onDelete = ForeignKey.CASCADE // This is the magic line
        )
    ]
)
data class TransactionCurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val transactionId: Long,
    val currency: String,
    val price: String,
    val timestamp: Long
)