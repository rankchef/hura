package com.example.hura.ui.transaction.components

import TransactionCard
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.hura.domain.model.TransactionView
import com.example.hura.ui.theme.AppTypography
import com.example.hura.ui.theme.Spacing
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
    onDeleteTransaction: (TransactionView) -> Unit
) {
    if (transactions.isEmpty()) {
        EmptyTransactionState()
        return
    }

    val sortedTransactions = transactions.sortedByDescending { it.timestamp }

    LazyColumn(
        contentPadding = PaddingValues(
            horizontal = Spacing.lg,
            vertical = Spacing.md
        )
    ) {
        var lastDate: LocalDate? = null

        sortedTransactions.forEachIndexed { index, transaction ->

            val transactionDate = transaction.timestamp
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            if (transactionDate != lastDate) {
                stickyHeader {
                    DateHeader(transactionDate)
                }
                lastDate = transactionDate
            }

            item(key = transaction.id) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TransactionCard(
                        transaction = transaction,
                        onCardClick = onTransactionClick,
                        onMerchantClick = onMerchantClick,
                        onCategoryClick = onCategoryClick,
                        onDelete = onDeleteTransaction
                    )

                    // Reduced spacing: smaller between same-date cards and date-separated sections
                    val nextTransactionDate = sortedTransactions.getOrNull(index + 1)
                        ?.timestamp?.atZone(ZoneId.systemDefault())?.toLocalDate()

                    Spacer(
                        modifier = Modifier.height(
                            if (nextTransactionDate != null && nextTransactionDate != transactionDate)
                                Spacing.md  // spacing between different dates
                            else
                                Spacing.sm  // spacing between same-date cards
                        )
                    )
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
        else -> date.format(DateTimeFormatter.ofPattern("MMM dd"))
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = Spacing.xs,   // reduced top padding
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
private fun EmptyTransactionState() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No transactions yet",
            style = AppTypography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}