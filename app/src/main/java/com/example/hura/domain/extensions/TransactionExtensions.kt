package com.example.hura.domain.extensions

import com.example.hura.domain.model.BarData
import com.example.hura.domain.model.HistoryRange
import com.example.hura.domain.model.TransactionGroup
import com.example.hura.domain.model.TransactionSort
import com.example.hura.domain.statistics.CardStatisticsInfo
import com.example.hura.transactionprocessor.StatisticsProcessor
import com.example.hura.ui.transaction.TransactionView
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

// --- FILTERING ---

fun List<TransactionView>.filterByMerchant(merchantId: Long): List<TransactionView> =
    this.filter { it.merchantId == merchantId }

fun List<TransactionView>.filterByCategory(categoryId: Long): List<TransactionView> =
    this.filter { it.categoryId == categoryId }

fun List<TransactionView>.filterByMinAmount(min: BigDecimal): List<TransactionView> =
    this.filter { item ->
        val amount = item.exchangeAmount?.toBigDecimalOrNull() ?: BigDecimal.ZERO
        amount >= min
    }

fun List<TransactionView>.filterByHistoryRange(range: HistoryRange): List<TransactionView> {
    if (range == HistoryRange.ALL_TIME) return this

    val userZone = ZoneId.systemDefault()
    val now = ZonedDateTime.ofInstant(Instant.now(), userZone)

    val startTime = when (range) {
        HistoryRange.LAST_7_DAYS -> now.minusWeeks(1).toInstant()
        HistoryRange.LAST_30_DAYS -> now.minusMonths(1).toInstant()
        HistoryRange.THIS_MONTH -> now.withDayOfMonth(1)
            .with(LocalTime.MIN)
            .toInstant()
        HistoryRange.THIS_YEAR -> now.withDayOfYear(1)
            .with(LocalTime.MIN)
            .toInstant()
        else -> Instant.MIN
    }

    return this.filter { it.timestamp >= startTime }
}

// --- SORTING & STATS ---

fun List<TransactionView>.orderBy(sort: TransactionSort): List<TransactionView> =
    when (sort) {
        TransactionSort.DATE_ASC -> this.sortedBy { it.timestamp }
        TransactionSort.DATE_DESC -> this.sortedByDescending { it.timestamp }
        TransactionSort.AMOUNT_ASC -> this.sortedBy { it.exchangeAmount?.toBigDecimalOrNull() ?: BigDecimal.ZERO }
        TransactionSort.AMOUNT_DESC -> this.sortedByDescending { it.exchangeAmount?.toBigDecimalOrNull() ?: BigDecimal.ZERO }
    }

fun List<TransactionView>.getCardStatistics(range: HistoryRange, currency: String): CardStatisticsInfo {
    return StatisticsProcessor.calculateCardStatistics(this, currency, range)
}

// --- GROUPING (For the UI List) ---

fun List<TransactionView>.groupByDay(): List<TransactionGroup> {
    val formatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy")
    return this.groupBy {
        it.timestamp.atZone(ZoneId.systemDefault()).toLocalDate()
    }.map { (date, transactions) ->
        TransactionGroup(
            title = date.format(formatter),
            transactions = transactions.sortedByDescending { it.timestamp },
            totalAmount = transactions.sumOf { it.amount }
        )
    }.sortedByDescending { it.transactions.first().timestamp }
}
