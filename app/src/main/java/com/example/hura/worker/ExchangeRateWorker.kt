package com.example.hura.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.hura.HuraApplication

class ExchangeRateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {

            val app = applicationContext as HuraApplication
            val repository = app.exchangeRateRepository
            repository.refreshExchangeRates()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}