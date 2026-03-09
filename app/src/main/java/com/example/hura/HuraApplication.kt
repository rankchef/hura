package com.example.hura

import android.app.Application
import androidx.room.Room
import com.example.hura.data.local.AppDatabase
import com.example.hura.data.repository.RoomCategoryRepository
import com.example.hura.data.repository.RoomMerchantRepository
import com.example.hura.data.repository.RoomTransactionRepository
import com.example.hura.data.repository.TransactionRepository
class HuraApplication : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "hura_db"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }


    val merchantRepository by lazy {
        RoomMerchantRepository(database.merchantDao())
    }

    val categoryRepository by lazy {
        RoomCategoryRepository(database.categoryDao())
    }

    val transactionRepository: TransactionRepository by lazy {
        RoomTransactionRepository(
            transactionDao = database.transactionDao(),
            merchantRepository = merchantRepository
        )
    }
}