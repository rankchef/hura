package com.example.hura.domain.model

import java.time.LocalDate

data class BarData(
    val label: String,
    val value: Double,
    val fullDate: LocalDate
)