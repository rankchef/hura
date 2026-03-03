package com.example.hura.data.repository

import com.example.hura.data.local.TransactionDao
import com.example.hura.data.local.TransactionEntity
import com.example.hura.domain.model.ParsedTransaction
import com.example.hura.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.time.Instant

class RoomTransactionRepository(
   private val dao: TransactionDao
) : TransactionRepository {

    override suspend fun insert(transaction: ParsedTransaction) {
        dao.insert(transaction.toEntity())
    }

    override fun observeAll(): Flow<List<ParsedTransaction>> {
        return dao.observeAll()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }
}

private fun ParsedTransaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        amount = amount.toPlainString(),
        currency = currency,
        timestamp = timestamp.toEpochMilli(),
        type = type.name,
        merchant = merchant ?: "",
        notificationKey = notificationKey
    )
}

private fun TransactionEntity.toDomain(): ParsedTransaction {
    return ParsedTransaction(
        amount = BigDecimal(amount),
        currency = currency,
        timestamp = Instant.ofEpochMilli(timestamp),
        type = TransactionType.valueOf(type),
        merchant = merchant.ifBlank { null },
        sourcePackage = "",
        notificationKey = notificationKey
    )
}