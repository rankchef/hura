package com.example.hura.domain.model

import com.example.hura.ui.transaction.TransactionView
import java.math.BigDecimal

data class TransactionGroup(
    val title: String,
    val transactions: List<TransactionView>,
    val totalAmount: BigDecimal
)