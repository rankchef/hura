package com.example.hura.data.repository

import com.example.hura.data.local.entity.TransactionEntity
import com.example.hura.domain.model.ParsedTransaction
import com.example.hura.domain.model.TransactionView
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun insert(transaction: ParsedTransaction)
    suspend fun softDelete(transactionId: Long)
    fun observeAll(): Flow<List<TransactionEntity>>
    fun observeAllWithDeleted(): Flow<List<TransactionEntity>>

    fun observeAllView(): Flow<List<TransactionView>>


}