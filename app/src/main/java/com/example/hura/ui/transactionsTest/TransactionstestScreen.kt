package com.example.hura.ui.transactionsTest

import TransactionsTestViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hura.data.local.TransactionEntity
import com.example.hura.domain.model.ParsedTransaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsTestScreen(
    viewModel: TransactionsTestViewModel = viewModel()
) {
    val transactions by viewModel.transactions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Transactions Test") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addDummyTransaction() }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(transactions) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: ParsedTransaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = transaction.merchant?: "Unknown", style = MaterialTheme.typography.titleMedium)
            Text(text = "Amount: ${transaction.amount} ${transaction.currency}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Timestamp: ${transaction.timestamp}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}