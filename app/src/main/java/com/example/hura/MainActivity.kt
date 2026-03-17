package com.example.hura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.hura.data.repository.TransactionRepository
import com.example.hura.domain.model.TransactionType
import com.example.hura.domain.model.TransactionView
import com.example.hura.ui.transaction.components.TransactionList
import com.example.hura.ui.theme.HuraTheme
import com.example.hura.ui.transaction.TransactionViewModel
import java.math.BigDecimal
import java.time.Instant

class MainActivity : ComponentActivity() {

    private val transactionRepository: TransactionRepository
        get() = (application as HuraApplication).transactionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as HuraApplication

        val viewModel = TransactionViewModel(
            transactionRepository = app.transactionRepository
        )

        setContent {
            HuraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState = viewModel.uiState.collectAsState().value

                    TransactionList(
                        transactions = uiState.transactions,
                        onTransactionClick = {},
                        onMerchantClick = {},
                        onCategoryClick = {},
                        onDeleteTransaction = {tr -> viewModel.deleteTransaction(tr)}
                    )
                }
            }
        }
    }
}

