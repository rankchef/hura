package com.example.hura.ui.main

import androidx.compose.ui.graphics.SolidColor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hura.domain.extensions.*
import com.example.hura.domain.model.HistoryRange
import com.example.hura.domain.model.TransactionSort
import com.example.hura.domain.repository.TransactionRepository
import com.example.hura.domain.statistics.CardStatisticsInfo
import ir.ehsannarmani.compose_charts.models.Bars
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import com.example.hura.transactionprocessor.StatisticsProcessor
import kotlinx.coroutines.flow.map


class MainViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    private val exchangeCurrency = "MKD"
    private val baseTransactions = repository.observeAll(exchangeCurrency)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedRange = MutableStateFlow(HistoryRange.THIS_MONTH)
    val selectedRange = _selectedRange.asStateFlow()


    val transactionList = baseTransactions.map { l ->
        l.orderBy(TransactionSort.DATE_DESC)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    val cardStats = combine(
        baseTransactions,
        _selectedRange
    ) { allTransactions, range ->
        StatisticsProcessor.calculateCardStatistics(
            allTransactions = allTransactions,
            currency = exchangeCurrency,
            range = range
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CardStatisticsInfo() // Empty UI state while loading
    )

    fun updateRange(range: HistoryRange) {
        _selectedRange.value = range
    }
    fun deleteTransaction(transactionId: Long) {
        viewModelScope.launch {
            repository.softDelete(transactionId)
        }
    }
}