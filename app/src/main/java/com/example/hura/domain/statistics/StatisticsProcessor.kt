package com.example.hura.transactionprocessor

import com.example.hura.domain.extensions.filterByHistoryRange
import com.example.hura.domain.model.HistoryRange
import com.example.hura.domain.model.TransactionType // Assuming this is your enum
import com.example.hura.domain.statistics.CardStatisticsInfo
import com.example.hura.domain.statistics.StatValue
import com.example.hura.domain.statistics.TransactionStat
import com.example.hura.ui.transaction.TransactionView
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

object StatisticsProcessor {

    fun calculateCardStatistics(
        allTransactions: List<TransactionView>,
        currency: String,
        range: HistoryRange
    ): CardStatisticsInfo {
        // 1. Slice by Date AND Filter only Expenses
        val currentPeriod = allTransactions
            .filterByHistoryRange(range)
            .filter { it.type == TransactionType.EXPENSE } // <-- ONLY EXPENSES

        val previousPeriod = getPreviousPeriodData(allTransactions, range)
            .filter { it.type == TransactionType.EXPENSE } // <-- ONLY EXPENSES

        // 2. Safely sum the exchange amounts using BigDecimal
        val currentTotal = currentPeriod.sumOf {
            it.exchangeAmount?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        }
        val previousTotal = previousPeriod.sumOf {
            it.exchangeAmount?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        }

        val count = currentPeriod.size
        val average = if (count > 0) {
            currentTotal.divide(BigDecimal(count), 2, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO
        }

        // 4. Calculate Trends & Insights
        val trendPercentage = calculateTrend(currentTotal, previousTotal)
        val countTrend = count - previousPeriod.size

        // Find the most frequent expense category
        val topCategory = currentPeriod
            .groupBy { it.categoryName ?: "Uncategorized" }
            .maxByOrNull { it.value.size }?.key

        // Helper to format currency (e.g., "42.50 EUR")
        val formatVal: (BigDecimal) -> String = { "%.2f %s".format(it.toDouble(), currency) }

        // 5. Construct the rich data class
        return CardStatisticsInfo(
            range = range,
            totalSpent = StatValue(
                formattedValue = formatVal(currentTotal),
                rawValue = currentTotal.toDouble(),
                trendPercentage = trendPercentage,
                isIncreaseBad = true // It's an expense, so going up is "Bad" (Red)
            ),
            averageSpent = StatValue(
                formattedValue = formatVal(average),
                rawValue = average.toDouble(),
                trendPercentage = null,
                isIncreaseBad = true
            ),
            transactions = TransactionStat(
                count = count,
                topCategory = topCategory,
                countTrend = if (range == HistoryRange.ALL_TIME) null else countTrend
            )
        )
    }

    private fun calculateTrend(current: BigDecimal, previous: BigDecimal): Double? {
        if (previous.compareTo(BigDecimal.ZERO) == 0) return null

        val difference = current.subtract(previous)
        val trend = difference.divide(previous, 4, RoundingMode.HALF_UP).multiply(BigDecimal(100))

        return trend.setScale(1, RoundingMode.HALF_UP).toDouble()
    }

    private fun getPreviousPeriodData(
        all: List<TransactionView>,
        range: HistoryRange
    ): List<TransactionView> {
        if (range == HistoryRange.ALL_TIME) return emptyList()

        val userZone = ZoneId.systemDefault()
        val now = ZonedDateTime.ofInstant(Instant.now(), userZone)

        val startAndEnd: Pair<Instant, Instant> = when (range) {
            HistoryRange.LAST_7_DAYS -> {
                now.minusDays(14).toInstant() to now.minusDays(7).toInstant()
            }
            HistoryRange.LAST_30_DAYS -> {
                now.minusDays(60).toInstant() to now.minusDays(30).toInstant()
            }
            HistoryRange.THIS_MONTH -> {
                val prevMonth = now.minusMonths(1)
                prevMonth.withDayOfMonth(1).with(LocalTime.MIN).toInstant() to
                        now.withDayOfMonth(1).with(LocalTime.MIN).toInstant()
            }
            HistoryRange.THIS_YEAR -> {
                val prevYear = now.minusYears(1)
                prevYear.withDayOfYear(1).with(LocalTime.MIN).toInstant() to
                        now.withDayOfYear(1).with(LocalTime.MIN).toInstant()
            }
            HistoryRange.ALL_TIME -> Instant.MIN to Instant.MIN
        }

        return all.filter { it.timestamp >= startAndEnd.first && it.timestamp < startAndEnd.second }
    }
}