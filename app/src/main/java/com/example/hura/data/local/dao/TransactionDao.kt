package com.example.hura.data.local.dao

import TransactionWithMerchantAndCategory
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.hura.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(transaction: TransactionEntity)

    @Query("UPDATE transactions SET isDeleted = 1 WHERE id = :id")
    suspend fun softDelete(id: Long)

    @Query("SELECT * FROM transactions WHERE isDeleted = 0 ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun observeAllWithDeleted(): Flow<List<TransactionEntity>>

    @Transaction
    @Query("""
    SELECT t.id AS transaction_id,
           t.amount,
           t.currency,
           t.merchantId,
           t.bankName,
           t.timestamp,
           t.notificationKey,
           t.type,
           t.isDeleted,
           m.id AS merchant_id,
           m.rawName AS merchant_rawName,
           m.nickname AS merchant_nickname,
           m.categoryId AS merchant_categoryId,
           c.id AS category_id,
           c.name AS category_name,
           c.iconId AS category_iconId
    FROM transactions t
    LEFT JOIN merchants m ON t.merchantId = m.id
    LEFT JOIN categories c ON m.categoryId = c.id
""")
    fun observeAllView(): Flow<List<TransactionWithMerchantAndCategory>>
}