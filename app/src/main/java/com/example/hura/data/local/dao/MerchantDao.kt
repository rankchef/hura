package com.example.hura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hura.data.local.entity.MerchantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MerchantDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(merchant: MerchantEntity): Long

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertAll(merchants: List<MerchantEntity>)

    @Query("SELECT * FROM merchants ORDER BY rawName ASC")
    fun getAll(): Flow<List<MerchantEntity>>

    @Query("SELECT * FROM merchants WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): MerchantEntity?

    @Query("SELECT * FROM merchants WHERE rawName = :rawName LIMIT 1")
    suspend fun getByRawName(rawName: String): MerchantEntity?
}