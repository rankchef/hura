package com.example.hura.data.repository

import com.example.hura.domain.model.ParsedTransaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun insert(transaction: ParsedTransaction)
    fun observeAll(): Flow<List<ParsedTransaction>>
}