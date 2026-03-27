package com.example.hura.ui.merchant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hura.R
import com.example.hura.ui.theme.Spacing
import com.example.hura.ui.transaction.TransactionView
import com.example.hura.ui.transaction.components.TransactionList // Added import

@Composable
fun MerchantDetailsScreen(
    merchant: MerchantUiModel,
    onNavigateBack: () -> Unit,
    onCategorizeClick: () -> Unit,
    onRenameMerchant: (String) -> Unit,
    onTransactionClick: (TransactionView) -> Unit,
    onCategoryClick: (TransactionView) -> Unit,
    onDeleteTransaction: (TransactionView) -> Unit
) {
    var showRenameDialog by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->
        // Wrapper Box to handle the Scaffold padding and background color
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TransactionList(
                transactions = merchant.transactions,
                onTransactionClick = onTransactionClick,
                onMerchantClick = { },
                onCategoryClick = onCategoryClick,
                onDeleteTransaction = onDeleteTransaction,
                showDateHeaders = false,
                header = {
                    // Profile Section inside the Header Slot
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Spacing.xl, bottom = Spacing.md),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable { onCategorizeClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = merchant.categoryIcon ?: R.drawable.ic_support),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.height(Spacing.md))

                        Text(
                            text = merchant.categoryName ?: "Uncategorized",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { onCategorizeClick() }
                                .padding(horizontal = Spacing.md, vertical = Spacing.xs)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { showRenameDialog = true }
                                .padding(Spacing.sm)
                        ) {
                            Text(
                                text = merchant.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(Spacing.xs))
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }

                        Spacer(modifier = Modifier.height(Spacing.xl))

                        // Transactions Title (Moved here from the old item block)
                        // Note: Removed horizontal padding because TransactionList already
                        // applies horizontal contentPadding to everything inside it.
                        Text(
                            text = "Recent Transactions",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = Spacing.sm)
                        )
                    }
                }
            )
        }
    }

    if (showRenameDialog) {
        MerchantRenameDialog(
            currentName = merchant.name,
            onDismiss = { showRenameDialog = false },
            onConfirm = {
                onRenameMerchant(it)
                showRenameDialog = false
            }
        )
    }
}