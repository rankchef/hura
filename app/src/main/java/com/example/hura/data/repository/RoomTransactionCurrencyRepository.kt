package com.example.hura.data.repository

import com.example.hura.data.local.dao.TransactionCurrencyDao
import com.example.hura.data.local.entity.TransactionCurrencyEntity
import com.example.hura.domain.repository.TransactionCurrencyRepository
import kotlinx.coroutines.flow.Flow

class RoomTransactionCurrencyRepository(
    private val dao: TransactionCurrencyDao
) : TransactionCurrencyRepository {

    override suspend fun insertTransactionCurrencies(entries: List<TransactionCurrencyEntity>) {
        dao.insertAll(entries)
    }

    override fun getTransactionCurrencies(transactionId: Long): Flow<List<TransactionCurrencyEntity>> {
        return dao.getByTransactionId(transactionId)
    }

    override fun getAllTransactionsInCurrency(currency: String): Flow<List<TransactionCurrencyEntity>> {
        return dao.getAllTransactionsInCurrency(currency)
    }

    override fun getTransactionsByMerchantAndCurrency(
        merchantId: Long,
        currency: String
    ): Flow<List<TransactionCurrencyEntity>> {
        return dao.getTransactionsByMerchantAndCurrency(merchantId, currency)
    }
}