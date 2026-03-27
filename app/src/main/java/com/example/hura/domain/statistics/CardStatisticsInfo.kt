package com.example.hura.domain.statistics

import com.example.hura.domain.model.HistoryRange
import java.math.BigDecimal

data class CardStatisticsInfo(
    val range: HistoryRange = HistoryRange.THIS_MONTH,
    val totalSpent: StatValue = StatValue(),
    val averageSpent: StatValue = StatValue(),
    val transactions: TransactionStat = TransactionStat()
)

data class StatValue(
    val formattedValue: String = "0.00",
    val rawValue: Double = 0.0,
    val trendPercentage: Double? = null, // e.g., 12.5 for +12.5%
    val isIncreaseBad: Boolean = true   // For spending, an increase is usually "red"
) {
    val trendStatus: TrendStatus
        get() = when {
            trendPercentage == null || trendPercentage == 0.0 -> TrendStatus.NEUTRAL
            trendPercentage > 0 -> if (isIncreaseBad) TrendStatus.NEGATIVE else TrendStatus.POSITIVE
            else -> if (isIncreaseBad) TrendStatus.POSITIVE else TrendStatus.NEGATIVE
        }
}

data class TransactionStat(
    val count: Int = 0,
    val topCategory: String? = null,
    val countTrend: Int? = null
)

enum class TrendStatus {
    POSITIVE, // Green
    NEGATIVE, // Red
    NEUTRAL   // Gray
}