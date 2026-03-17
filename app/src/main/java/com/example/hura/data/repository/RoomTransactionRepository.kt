package com.example.hura.data.repository

import TransactionWithMerchantAndCategory
import com.example.hura.data.local.dao.TransactionDao
import com.example.hura.data.local.entity.TransactionCurrencyEntity
import com.example.hura.data.local.entity.TransactionEntity
import com.example.hura.domain.currency.CurrencyConverter
import com.example.hura.domain.model.ParsedTransaction
import com.example.hura.domain.model.TransactionType
import com.example.hura.domain.model.TransactionView
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

        transactionDao.insert(entity)

        //Get latest EUR-based rates
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
                transactionId = entity.id.toString(),
                currency = currencyCode.uppercase(),
                price = convertedAmount.toPlainString(),
                timestamp = transaction.timestamp.toEpochMilli()
            )
        }

        //Insert all converted currency rows
        transactionCurrencyRepository.insertTransactionCurrencies(currencyEntries)
    }

    override fun observeAll(): Flow<List<TransactionEntity>> {
        return transactionDao.observeAll()
    }

    override fun observeAllWithDeleted(): Flow<List<TransactionEntity>> {
        return transactionDao.observeAllWithDeleted()
    }

    override suspend fun softDelete(transactionId: Long) {
        transactionDao.softDelete(transactionId)
    }

    override fun observeAllView(): Flow<List<TransactionView>> {
        return transactionDao.observeAllView().map { list ->
            list.map { relation ->
                relation.toView()
            }
        }
    }

    private fun TransactionWithMerchantAndCategory.toView(): TransactionView {
        return TransactionView(
            id = transaction_id,
            amount = BigDecimal(amount),
            timestamp = Instant.ofEpochMilli(timestamp),
            currency = currency,
            type = TransactionType.valueOf(type),
            bankName = bankName,
            merchantId = merchant_id,
            merchantName = merchant_nickname ?: merchant_rawName,
            categoryId = category_id,
            categoryName = category_name,
            categoryIconId = category_iconId
        )
    }
}
