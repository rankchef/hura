package com.example.hura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hura.data.local.entity.ExchangeRateEntity
import java.time.Instant

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rates: List<ExchangeRateEntity>)

    @Query("""
        SELECT * FROM exchange_rates
        WHERE timestamp = (SELECT MAX(timestamp) FROM exchange_rates)
    """)
    suspend fun getLatestRates(): List<ExchangeRateEntity>
}