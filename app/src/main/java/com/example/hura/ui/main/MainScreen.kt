package com.example.hura.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hura.R
import com.example.hura.domain.model.HistoryRange
import com.example.hura.domain.statistics.TrendStatus
import com.example.hura.ui.generalcomponents.RangeSelector
import com.example.hura.ui.generalcomponents.StatisticsCard
import com.example.hura.ui.transaction.components.TransactionList

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val list by viewModel.transactionList.collectAsStateWithLifecycle()
    val selectedRange by viewModel.selectedRange.collectAsStateWithLifecycle()
    val cardStats by viewModel.cardStats.collectAsStateWithLifecycle()

    TransactionList(
        transactions = list,
        onTransactionClick = {},
        onMerchantClick = {},
        onCategoryClick = {},
        onDeleteTransaction = { tr ->
            viewModel.deleteTransaction(tr.id)
        },
        header = {
            // 1. TOP SELECTOR
            RangeSelector(
                selectedRange = selectedRange,
                onRangeSelected = { s -> viewModel.updateRange(s) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. STATISTICS GRID/COLUMN
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val periodLabel = when (selectedRange) {
                    HistoryRange.LAST_7_DAYS -> "last week"
                    HistoryRange.LAST_30_DAYS -> "last 30 days"
                    HistoryRange.THIS_MONTH -> "last month"
                    HistoryRange.THIS_YEAR -> "last year"
                    else -> ""
                }

                // --- CARD 1: TOTAL SPENDING ---
                StatisticsCard(
                    title = "Total Spending",
                    iconId = R.drawable.ic_stats_total,
                    value = cardStats.totalSpent.formattedValue
                ) {
                    cardStats.totalSpent.trendPercentage?.let { trend ->
                        if (periodLabel.isNotEmpty()) {
                            val isIncrease = trend > 0
                            val trendColor = if (isIncrease) MaterialTheme.colorScheme.error else Color(0xFF4CAF50)

                            Text(
                                text = "${if (isIncrease) "+" else ""}$trend% vs $periodLabel",
                                style = MaterialTheme.typography.labelSmall,
                                color = trendColor,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                // --- CARD 2: AVERAGE SPENT ---
                StatisticsCard(
                    title = "Average Spent",
                    iconId = R.drawable.ic_stats_division,
                    value = cardStats.averageSpent.formattedValue
                ) {
                    Text(
                        text = "Per transaction",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // --- CARD 3: TRANSACTIONS ---
                StatisticsCard(
                    title = "Transactions",
                    iconId = R.drawable.ic_receipt_long,
                    value = cardStats.transactions.count.toString()
                ) {
                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        // Top Category
                        cardStats.transactions.topCategory?.let { category ->
                            Text(
                                text = "Mostly $category",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Count Trend
                        if (periodLabel.isNotEmpty()) {
                            val diff = cardStats.transactions.countTrend ?: 0
                            Text(
                                text = "${if (diff >= 0) "+" else ""}$diff transactions vs $periodLabel",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}