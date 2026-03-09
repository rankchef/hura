package com.example.hura.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    indices = [
        Index(value = ["notificationKey"], unique = true),
        Index(value = ["merchantId"]),
        Index(value = ["bankName"]),
        Index(value = ["timestamp"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = MerchantEntity::class,
            parentColumns = ["id"],
            childColumns = ["merchantId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)

data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: String, //store BigDecimal as String
    val currency: String,
    val merchantId: Long,
    val bankName: String,
    val timestamp: Long,
    val notificationKey: String? = null,
    val type: String, //store enum as String
    val isDeleted: Boolean = false
)