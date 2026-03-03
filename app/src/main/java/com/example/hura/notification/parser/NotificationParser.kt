package com.example.hura.notification.parser

import com.example.hura.domain.model.ParsedTransaction
import com.example.hura.notification.model.RawNotification

interface NotificationParser {
    fun canHandle(packageName: String): Boolean
    fun parse(notification: RawNotification): ParsedTransaction?
}