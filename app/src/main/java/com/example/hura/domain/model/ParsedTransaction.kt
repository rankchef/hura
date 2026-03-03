package com.example.hura.domain.model
import java.math.BigDecimal
import java.time.Instant

data class ParsedTransaction(
    val amount: BigDecimal,
    val currency: String,
    val timestamp: Instant,
    val type: TransactionType,
    val merchant: String?,
    val sourcePackage: String,
    val notificationKey: String
)

enum class TransactionType{
    INCOME,
    EXPENSE
}