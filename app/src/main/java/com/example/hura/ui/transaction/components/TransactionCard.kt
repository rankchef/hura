package com.example.hura.ui.transaction.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hura.domain.model.TransactionType
import com.example.hura.ui.category.CategoryItem
import com.example.hura.ui.theme.*
import com.example.hura.ui.transaction.TransactionView
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionCard(
    transaction: TransactionView,
    onCardClick: (TransactionView) -> Unit,
    onMerchantClick: (TransactionView) -> Unit,
    onCategoryClick: (TransactionView) -> Unit,
    onDelete: (TransactionView) -> Unit,
    modifier: Modifier = Modifier // Added Modifier parameter
) {
    val dateText = transaction.timestamp
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
        .format(DateTimeFormatter.ofPattern("MMM dd, h:mm a"))

    val categoryName = transaction.categoryName ?: "Uncategorized"

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete(transaction)
                true
            } else false
        }
    )

    LaunchedEffect(transaction.id) {
        dismissState.reset()
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier, // Applied modifier here to control the whole component's layout
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                else -> Color.Transparent
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(Shapes.large)
                    .background(color)
                    .padding(horizontal = Spacing.lg),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(IconSizes.medium)
                )
            }
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCardClick(transaction) },
            shape = Shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = Elevation.level1),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(Spacing.md)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Spacing.md)
            ) {

                // Category Icon Container
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                        .clickable { onCategoryClick(transaction) },
                    contentAlignment = Alignment.Center
                ) {
                    CategoryItem(
                        iconRes = transaction.categoryIconId,
                        isSelected = false,
                        shape = CircleShape,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        iconTint = MaterialTheme.colorScheme.primary,
                        onClick = { onCategoryClick(transaction) }
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = transaction.merchantName,
                            style = AppTypography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onMerchantClick(transaction) }
                                .padding(end = Spacing.sm)
                        )

                        Text(
                            text = buildAnnotatedString {
                                val amountPrefix = if (transaction.type == TransactionType.EXPENSE) "-" else ""
                                val amountValue = transaction.amount.setScale(2, RoundingMode.HALF_UP).toPlainString()

                                withStyle(style = AppTypography.titleMedium.toSpanStyle()) {
                                    append("$amountPrefix$amountValue ")
                                }
                                withStyle(style = AppTypography.labelSmall.toSpanStyle()) {
                                    append(transaction.currency.uppercase())
                                }
                            },
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Spacing.xs),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = Spacing.sm)
                        ) {
                            val typeString = transaction.type.name.lowercase().replaceFirstChar { it.uppercase() }

                            Text(
                                text = "${transaction.bankName} • $typeString",
                                style = AppTypography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = categoryName,
                                style = AppTypography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(top = Spacing.xs)
                                    .clickable { onCategoryClick(transaction) }
                            )
                        }

                        Text(
                            text = dateText,
                            style = AppTypography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    val transactionView = TransactionView(
        id = 0,
        amount = BigDecimal("15.50"),
        currency = "USD",
        type = TransactionType.EXPENSE,
        timestamp = Instant.now(),
        bankName = "Chase Bank",
        merchantId = 0,
        merchantName = "Starbucks",
        categoryId = null,
        categoryName = "Food & Drink",
        categoryIconId = null
    )

    HuraTheme {
        TransactionCard(
            transaction = transactionView,
            onCardClick = {},
            onCategoryClick = {},
            onMerchantClick = {},
            onDelete = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}