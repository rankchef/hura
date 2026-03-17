package com.example.hura.ui.transaction

import com.example.hura.domain.model.TransactionView

data class TransactionsUiState(
    val transactions: List<TransactionView> = emptyList()
)