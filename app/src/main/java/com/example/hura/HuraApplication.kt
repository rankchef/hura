package com.example.hura

import android.app.Application
import androidx.room.Room
import com.example.hura.data.local.AppDatabase
import com.example.hura.data.repository.RoomTransactionRepository
import com.example.hura.data.repository.TransactionRepository
class HuraApplication : Application() {
    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "hura_db"
        ).build()
    }

    val transactionRepository: TransactionRepository by lazy {
        RoomTransactionRepository(database.transactionDao())
    }
}