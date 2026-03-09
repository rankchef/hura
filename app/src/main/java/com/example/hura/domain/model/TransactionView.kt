package com.example.hura.domain.model

import java.math.BigDecimal
import java.time.Instant

data class TransactionView(
    val id: Long,
    val amount: BigDecimal,
    val currency: String,
    val type: TransactionType,
    val timestamp: Instant,
    val bankName: String,
    val merchantId: Long,
    val merchantName: String,
    val categoryId: Long?,
    val categoryName: String?,
    val categoryIconId: Int?
)