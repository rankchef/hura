package com.example.hura

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hura.data.local.entity.CategoryEntity
import com.example.hura.domain.model.TransactionType
import com.example.hura.ui.category.CategorizeMerchantBottomSheet
import com.example.hura.ui.main.MainScreen
import com.example.hura.ui.main.MainViewModel
import com.example.hura.ui.merchant.MerchantDetailsScreen
import com.example.hura.ui.merchant.MerchantUiModel
import com.example.hura.ui.theme.HuraTheme
import com.example.hura.ui.transaction.TransactionView
import java.math.BigDecimal
import java.time.Instant

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as HuraApplication
        val repo = app.transactionRepository

        // 2. Standard Android ViewModel initialization (No extra KTX libraries needed)
        // This 'Factory' is the bridge that lets you pass the 'repo' into the constructor
        val mainViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repo) as T
            }
        })[MainViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            HuraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        MainScreen(mainViewModel)
                    }
                }
            }
        }
    }
}