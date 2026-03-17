package com.example.hura.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hura.data.local.dao.CategoryDao
import com.example.hura.data.local.dao.ExchangeRateDao
import com.example.hura.data.local.dao.MerchantDao
import com.example.hura.data.local.dao.TransactionCurrencyDao
import com.example.hura.data.local.dao.TransactionDao
import com.example.hura.data.local.entity.CategoryEntity
import com.example.hura.data.local.entity.ExchangeRateEntity
import com.example.hura.data.local.entity.MerchantEntity
import com.example.hura.data.local.entity.TransactionCurrencyEntity
import com.example.hura.data.local.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        MerchantEntity::class,
        CategoryEntity::class,
        ExchangeRateEntity::class,
        TransactionCurrencyEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun merchantDao(): MerchantDao
    abstract fun categoryDao(): CategoryDao
    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun transactionCurrencyDao(): TransactionCurrencyDao
}
