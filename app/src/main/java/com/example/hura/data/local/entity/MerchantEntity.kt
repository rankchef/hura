package com.example.hura.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "merchants",
    indices = [
        Index(value = ["rawName"], unique = true),
        Index(value = ["categoryId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.Companion.SET_NULL
        )
    ]
)
data class MerchantEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val rawName: String,

    val nickname: String? = null,

    val categoryId: Long? = null
)