import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hura.R
import com.example.hura.domain.model.TransactionType
import com.example.hura.domain.model.TransactionView
import com.example.hura.ui.theme.AppTypography
import com.example.hura.ui.theme.Elevation
import com.example.hura.ui.theme.IconSizes
import com.example.hura.ui.theme.Shapes
import com.example.hura.ui.theme.Spacing
import com.example.hura.ui.theme.extendedColors
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
    onDelete: (TransactionView) -> Unit
) {

    val dateText = transaction.timestamp
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))

    val categoryName = transaction.categoryName ?: "Uncategorized"

    val categoryIconRes = transaction.categoryIconId ?: R.drawable.support

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete(transaction)
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
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
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCardClick(transaction) },
            shape = Shapes.large,
            elevation = CardDefaults.cardElevation(defaultElevation = Elevation.level1)
        ) {

            Row(
                modifier = Modifier
                    .padding(Spacing.md)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.sm),
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "${if (transaction.type == TransactionType.EXPENSE) "-" else ""}${transaction.amount.setScale(2, RoundingMode.HALF_UP)} ${transaction.currency}",
                        style = AppTypography.headlineMedium,
                        color = if (transaction.type == TransactionType.INCOME)
                            MaterialTheme.extendedColors.income.color
                        else
                            MaterialTheme.extendedColors.expense.color,
                        letterSpacing = (-0.5).sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
                        modifier = Modifier.clickable {
                            onMerchantClick(transaction)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Storefront,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(IconSizes.medium)
                        )

                        Text(
                            text = transaction.merchantName,
                            style = AppTypography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountBalance,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(IconSizes.medium)
                        )

                        Text(
                            text = transaction.bankName,
                            style = AppTypography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.xs),
                    modifier = Modifier.clickable {
                        onCategoryClick(transaction)
                    }
                ) {

                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = categoryIconRes),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Text(
                        text = categoryName,
                        style = AppTypography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = dateText,
                        style = AppTypography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CardPreview() {

    val transactionView = TransactionView(
        id = 0,
        amount = BigDecimal(612.00),
        currency = "EUR",
        type = TransactionType.EXPENSE,
        timestamp = Instant.now(),
        bankName = "Swedbank",
        merchantId = 0,
        merchantName = "Skims",
        categoryId = null,
        categoryName = null,
        categoryIconId = null
    )

    TransactionCard(
        transactionView,
        onCardClick = {},
        onCategoryClick = {},
        onMerchantClick = {},
        onDelete = {}
    )
}