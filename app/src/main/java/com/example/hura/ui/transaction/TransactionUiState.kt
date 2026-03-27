package com.example.hura.ui.transaction

import com.example.hura.ui.transaction.TransactionView

data class TransactionsUiState(
    val transactions: List<TransactionView> = emptyList()
)