package com.example.hura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.hura.data.local.entity.TransactionEntity
import com.example.hura.data.repository.TransactionRepository
import com.example.hura.domain.model.ParsedTransaction
import com.example.hura.domain.model.TransactionType
import com.example.hura.ui.theme.HuraTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Instant

class MainActivity : ComponentActivity() {

    private val transactionRepository: TransactionRepository
        get() = (application as HuraApplication).transactionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            HuraTheme {
                TransactionScreen(transactionRepository)
            }
        }
    }
}

@Composable
fun TransactionScreen(repository: TransactionRepository) {
    // collect transactions from DB
    val transactions = repository.observeAll().collectAsState(initial = emptyList())

    Scaffold {
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            items(transactions.value.size) { index ->
                val t = transactions.value[index]
                Text(
                    text = "Amount: ${t.amount}, MerchantId: ${t.merchantId}",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
