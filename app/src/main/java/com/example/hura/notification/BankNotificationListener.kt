package com.example.hura.notification

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.hura.HuraApplication
import com.example.hura.notification.engine.EngineResult
import com.example.hura.notification.engine.NotificationParserEngine
import com.example.hura.notification.engine.NotificationParsers
import com.example.hura.notification.model.RawNotification
import com.example.hura.notification.parser.NLBNotificationParser
import com.example.hura.notification.parser.NotificationParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant

class BankNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val parsers = NotificationParsers.parsers
        val packageName = sbn.packageName
        val notification = sbn.notification
        val extras = notification.extras
        val notificationKey = sbn.key
        val title = extras.getString("android.title")
        val text = extras.getCharSequence("android.text")?.toString()

        val rawNotification = RawNotification(packageName, notificationKey, title, text, Instant.now())

        val repository = (application as HuraApplication).transactionRepository

        CoroutineScope(Dispatchers.IO).launch {
            when (val result = NotificationParserEngine(parsers).process(rawNotification)) {
                is EngineResult.Success -> {
                    repository.insert(result.transaction)
                    Log.d("NOTIF_DEBUG", "Saved transaction: ${result.transaction}")
                }
                EngineResult.NotSupported -> Log.d("NOTIF_DEBUG", "Notification not supported")
                EngineResult.ParseFailed -> Log.d("NOTIF_DEBUG", "Failed to parse notification")
            }
        }
    }
}