package com.example.hura

import android.app.Application
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.hura.data.local.AppDatabase
import com.example.hura.data.repository.RoomCategoryRepository
import com.example.hura.data.repository.RoomExchangeRateRepository
import com.example.hura.data.repository.RoomMerchantRepository
import com.example.hura.data.repository.RoomTransactionCurrencyRepository
import com.example.hura.data.repository.RoomTransactionRepository
import com.example.hura.domain.repository.TransactionRepository
import com.example.hura.worker.ExchangeRateWorker
import java.util.concurrent.TimeUnit

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

    val exchangeRateRepository by lazy {
        RoomExchangeRateRepository(database.exchangeRateDao())
    }

    val transactionCurrencyRepository by lazy {
        RoomTransactionCurrencyRepository(database.transactionCurrencyDao())
    }

    val transactionRepository: TransactionRepository by lazy {
        RoomTransactionRepository(
            transactionDao = database.transactionDao(),
            merchantRepository = merchantRepository,
            exchangeRateRepository = exchangeRateRepository,
            transactionCurrencyRepository = transactionCurrencyRepository
        )
    }

    override fun onCreate() {
        super.onCreate()
        scheduleExchangeRateWorker()
    }

    private fun scheduleExchangeRateWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWork = PeriodicWorkRequestBuilder<ExchangeRateWorker>(24, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ExchangeRateWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWork
        )

        val oneTimeWork = OneTimeWorkRequestBuilder<ExchangeRateWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "ExchangeRateWorkerImmediate",
            ExistingWorkPolicy.KEEP,
            oneTimeWork
        )
    }
}