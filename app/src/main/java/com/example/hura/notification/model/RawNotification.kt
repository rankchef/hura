package com.example.hura.notification.model

import java.time.Instant

data class RawNotification(
    val packageName: String,
    val notificationKey: String,
    val title: String?,
    val text: String?,
    val timestamp: Instant
)
