package com.example.hura.data.repository

import com.example.hura.data.local.entity.TransactionCurrencyEntity

interface TransactionCurrencyRepository {

    suspend fun insertTransactionCurrencies(entries: List<TransactionCurrencyEntity>)
    suspend fun getTransactionCurrencies(transactionId: String): List<TransactionCurrencyEntity>
    suspend fun getAllTransactionsInCurrency(currency: String): List<TransactionCurrencyEntity>
}