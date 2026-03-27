package com.example.hura.data.repository

import TransactionWithMerchantAndCategory
import android.util.Log
import com.example.hura.data.local.dao.TransactionDao
import com.example.hura.data.local.entity.TransactionCurrencyEntity
import com.example.hura.data.local.entity.TransactionEntity
import com.example.hura.data.mapper.toView
import com.example.hura.domain.currency.CurrencyConverter
import com.example.hura.domain.model.ParsedTransaction
import com.example.hura.domain.model.TransactionType
import com.example.hura.domain.repository.ExchangeRateRepository
import com.example.hura.domain.repository.MerchantRepository
import com.example.hura.domain.repository.TransactionCurrencyRepository
import com.example.hura.domain.repository.TransactionRepository
import com.example.hura.ui.transaction.TransactionView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.time.Instant

class RoomTransactionRepository(
    private val transactionDao: TransactionDao,
    private val merchantRepository: MerchantRepository,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val transactionCurrencyRepository: TransactionCurrencyRepository
) : TransactionRepository {

    override suspend fun insert(transaction: ParsedTransaction) {
        //Insert the transaction itself
        val entity = TransactionEntity(
            amount = transaction.amount.toPlainString(),
            currency = transaction.currency.uppercase(),
            notificationKey = transaction.notificationKey,
            bankName = transaction.bankName,
            timestamp = transaction.timestamp.toEpochMilli(),
            type = transaction.type.name,
            merchantId = merchantRepository.getOrInsert(transaction.merchant).id
        )
        Log.d("HuraDebug", "Entity ID before DAO insert: ${entity.id}")
        val generatedId = transactionDao.insert(entity)

        val eurRates = exchangeRateRepository.getLatestRates()
            .mapValues { (_, rateEntity) -> rateEntity.rateToEur.toBigDecimal() }

        //Build one TransactionCurrencyEntity per supported currency
        val currencyEntries = eurRates.map { (currencyCode, rateToEur) ->
            val convertedAmount = CurrencyConverter.convert(
                amount = transaction.amount,
                fromCurrency = transaction.currency,
                toCurrency = currencyCode,
                eurRates = eurRates
            )

            TransactionCurrencyEntity(
                transactionId = generatedId,
                currency = currencyCode.uppercase(),
                price = convertedAmount.toPlainString(),
                timestamp = transaction.timestamp.toEpochMilli()
            )
        }

        //Insert all converted currency rows
        transactionCurrencyRepository.insertTransactionCurrencies(currencyEntries)
    }

    override suspend fun softDelete(transactionId: Long) {
        transactionDao.softDelete(transactionId)
    }

    override fun observeAll(targetCurrency: String): Flow<List<TransactionView>> {
        return transactionDao.observeAllViewWithCurrency(targetCurrency).map { list ->
            list.map { relation ->
                relation.toView()
            }
        }
    }
}
