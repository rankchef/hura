package com.example.hura.notification.engine

import com.example.hura.notification.model.RawNotification
import com.example.hura.notification.parser.NLBNotificationParser
import com.example.hura.notification.parser.NotificationParser
import com.example.hura.notification.parser.TestNotificationParser

class NotificationParserEngine(
    private val parsers: List<NotificationParser>
) {
    fun process(notification: RawNotification): EngineResult{
        val parser = parsers.firstOrNull{ it.canHandle(notification.packageName) }
            ?: return EngineResult.NotSupported
        val parsedTransaction = parser.parse(notification)
            ?: return EngineResult.ParseFailed
        return EngineResult.Success(parsedTransaction)
    }
}

object NotificationParsers {
    val parsers: List<NotificationParser> = listOf(
        NLBNotificationParser(),
        TestNotificationParser()
    )
}