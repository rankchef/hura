package com.example.hura.domain.repository

import com.example.hura.data.local.entity.TransactionCurrencyEntity
import kotlinx.coroutines.flow.Flow

interface TransactionCurrencyRepository {

    suspend fun insertTransactionCurrencies(entries: List<TransactionCurrencyEntity>)

    fun getTransactionCurrencies(transactionId: Long): Flow<List<TransactionCurrencyEntity>>

    fun getAllTransactionsInCurrency(currency: String): Flow<List<TransactionCurrencyEntity>>

    fun getTransactionsByMerchantAndCurrency(
        merchantId: Long,
        currency: String
    ): Flow<List<TransactionCurrencyEntity>>
}