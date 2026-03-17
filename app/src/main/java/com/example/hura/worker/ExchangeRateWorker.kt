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

    companion object {
        private const val TAG = """ExchangeRateWorker"""
    }

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Worker started")

            val app = applicationContext as HuraApplication
            val repository = app.exchangeRateRepository

            Log.d(TAG, "Refreshing exchange rates...")
            repository.refreshExchangeRates()
            Log.d(TAG, "Exchange rates successfully refreshed")

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Worker failed with exception: ${e.message}", e)
            Result.retry()
        }
    }
}