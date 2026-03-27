package com.example.hura.statistics

import com.example.hura.domain.model.HistoryRange
import java.math.BigDecimal

data class CardStatisticsInfo(
    val totalSpent: String = "0.00",
    val averageSpent: String = "0.00",
    val transactionCount: Int = 0,
    val range: HistoryRange = HistoryRange.ALL_TIME
)