package com.example.hura.ui.merchant

import com.example.hura.ui.transaction.TransactionView

data class MerchantUiModel(
    val id: Long,
    val name: String,
    val categoryId: Long?,
    val categoryName: String?,
    val categoryIcon: Int?,
    val transactions: List<TransactionView>
)
