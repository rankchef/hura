package com.example.hura.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hura.data.repository.TransactionRepository
import com.example.hura.domain.model.TransactionView
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val uiState: StateFlow<TransactionsUiState> =
        transactionRepository
            .observeAllView()
            .map { transactions ->
                TransactionsUiState(
                    transactions = transactions
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = TransactionsUiState()
            )

    fun deleteTransaction(transaction: TransactionView) {
        viewModelScope.launch {
            transactionRepository.softDelete(transaction.id)
        }
    }
}