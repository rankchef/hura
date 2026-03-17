package com.example.hura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hura.data.local.entity.TransactionCurrencyEntity
import com.example.hura.data.local.entity.TransactionEntity

@Dao
interface TransactionCurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<TransactionCurrencyEntity>)

    @Query("""
        SELECT tc.* FROM transaction_currencies AS tc
        INNER JOIN transactions AS t ON t.id = tc.transactionId
        WHERE tc.currency = :currency
          AND t.isDeleted = 0
    """)
    suspend fun getAllTransactionsInCurrency(currency: String): List<TransactionCurrencyEntity>

    @Query("SELECT * FROM transaction_currencies WHERE transactionId = :transactionId")
    suspend fun getByTransactionId(transactionId: String): List<TransactionCurrencyEntity>
}