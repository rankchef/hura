package com.example.hura.data.repository

import com.example.hura.data.local.dao.TransactionCurrencyDao
import com.example.hura.data.local.entity.TransactionCurrencyEntity

class RoomTransactionCurrencyRepository(
    private val dao: TransactionCurrencyDao
) : TransactionCurrencyRepository {

    override suspend fun insertTransactionCurrencies(entries: List<TransactionCurrencyEntity>) {
        dao.insertAll(entries)
    }

    override suspend fun getTransactionCurrencies(transactionId: String): List<TransactionCurrencyEntity> {
        return dao.getByTransactionId(transactionId)
    }

    override suspend fun getAllTransactionsInCurrency(currency: String): List<TransactionCurrencyEntity> {
        return dao.getAllTransactionsInCurrency(currency)
    }
}