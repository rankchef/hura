package com.example.hura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hura.data.local.entity.TransactionCurrencyEntity
import kotlinx.coroutines.flow.Flow

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
    fun getAllTransactionsInCurrency(currency: String): Flow<List<TransactionCurrencyEntity>>


    @Query("""
        SELECT tc.* FROM transaction_currencies AS tc
        INNER JOIN transactions AS t ON t.id = tc.transactionId
        WHERE t.merchantId = :merchantId 
          AND tc.currency = :currency
          AND t.isDeleted = 0
    """)
    fun getTransactionsByMerchantAndCurrency(merchantId: Long, currency: String): Flow<List<TransactionCurrencyEntity>>

    @Query("SELECT * FROM transaction_currencies WHERE transactionId = :transactionId")
    fun getByTransactionId(transactionId: Long): Flow<List<TransactionCurrencyEntity>>
}