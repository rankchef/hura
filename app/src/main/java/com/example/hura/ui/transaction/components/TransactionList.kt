package com.example.hura.ui.transaction.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hura.domain.model.TransactionType
import com.example.hura.ui.transaction.TransactionView
import com.example.hura.ui.theme.AppTypography
import com.example.hura.ui.theme.Spacing
import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionList(
    transactions: List<TransactionView>,
    onTransactionClick: (TransactionView) -> Unit,
    onMerchantClick: (TransactionView) -> Unit,
    onCategoryClick: (TransactionView) -> Unit,
    onDeleteTransaction: (TransactionView) -> Unit,
    showDateHeaders: Boolean = true,
    header: @Composable () -> Unit = {}
) {
    LazyColumn(
        contentPadding = PaddingValues(
            horizontal = Spacing.lg,
            vertical = Spacing.md
        ),
        modifier = Modifier.fillMaxSize()
    ) {
        item(key = "header_slot") {
            header()
        }

        if (transactions.isEmpty()) {
            item(key = "empty_state") {
                EmptyTransactionState(
                    modifier = Modifier.fillParentMaxSize()
                )
            }
        } else {
            val sortedTransactions = transactions.sortedByDescending { it.timestamp }
            var lastDate: LocalDate? = null

            sortedTransactions.forEachIndexed { index, transaction ->
                val transactionDate = transaction.timestamp
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                if (showDateHeaders && transactionDate != lastDate) {
                    stickyHeader(key = "date_header_$transactionDate") {
                        DateHeader(transactionDate)
                    }
                    lastDate = transactionDate
                }

                item(key = transaction.id) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                    ) {
                        TransactionCard(
                            transaction = transaction,
                            onCardClick = onTransactionClick,
                            onMerchantClick = onMerchantClick,
                            onCategoryClick = onCategoryClick,
                            onDelete = onDeleteTransaction
                        )

                        val nextTransactionDate = sortedTransactions.getOrNull(index + 1)
                            ?.timestamp?.atZone(ZoneId.systemDefault())?.toLocalDate()

                        Spacer(
                            modifier = Modifier.height(
                                if (nextTransactionDate != null && nextTransactionDate != transactionDate)
                                    Spacing.md
                                else
                                    Spacing.sm
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DateHeader(date: LocalDate) {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    val text = when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = Spacing.xs,
                bottom = Spacing.xs,
                start = Spacing.lg,
                end = Spacing.lg
            ),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = text,
            style = AppTypography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EmptyTransactionState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(Spacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No transactions yet",
            style = AppTypography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
