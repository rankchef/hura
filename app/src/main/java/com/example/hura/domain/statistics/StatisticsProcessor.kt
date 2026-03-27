package com.example.hura.transactionprocessor

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.example.hura.domain.model.HistoryRange
import com.example.hura.statistics.CardStatisticsInfo
import com.example.hura.ui.transaction.TransactionView
import ir.ehsannarmani.compose_charts.models.Bars
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

import java.time.Month
import java.time.format.TextStyle
import java.util.Locale
object StatisticsProcessor {

    fun calculateCardStatistics(
        filteredTransactions: List<TransactionView>,
        currency: String,
        range: HistoryRange
    ): CardStatisticsInfo {
        if (filteredTransactions.isEmpty()) return CardStatisticsInfo(range = range)

        val total = filteredTransactions.sumOf {
            it.exchangeAmount?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        }

        val count = filteredTransactions.size
        val average = if (count > 0) {
            total.divide(BigDecimal(count), 2, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO
        }

        return CardStatisticsInfo(
            totalSpent = "$total $currency",
            averageSpent = "$average $currency",
            transactionCount = count,
            range = range
        )
    }
}