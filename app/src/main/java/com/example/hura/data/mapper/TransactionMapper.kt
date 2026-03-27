package com.example.hura.data.mapper

import TransactionWithMerchantAndCategory
import com.example.hura.domain.model.TransactionType
import com.example.hura.ui.transaction.TransactionView
import java.math.BigDecimal
import java.time.Instant

fun TransactionWithMerchantAndCategory.toView(): TransactionView {
    return TransactionView(
        id = transaction_id,
        amount = BigDecimal(amount),
        timestamp = Instant.ofEpochMilli(timestamp),
        currency = currency,
        type = TransactionType.valueOf(type),
        bankName = bankName,
        merchantId = merchant_id,
        merchantName = merchant_nickname ?: merchant_rawName,
        categoryId = category_id,
        categoryName = category_name,
        categoryIconId = category_iconId,
        exchangeAmount = exchangeAmount,
        exchangeCurrency = exchangeCurrency
    )
}