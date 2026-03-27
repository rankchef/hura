package com.example.hura.domain.repository

import com.example.hura.data.local.entity.TransactionEntity
import com.example.hura.domain.model.ParsedTransaction
import com.example.hura.ui.transaction.TransactionView
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun insert(transaction: ParsedTransaction)
    suspend fun softDelete(transactionId: Long)
    fun observeAll(targetCurrency: String): Flow<List<TransactionView>>
}