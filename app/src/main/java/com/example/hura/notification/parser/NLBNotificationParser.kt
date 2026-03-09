package com.example.hura.notification.parser

import com.example.hura.domain.model.ParsedTransaction
import com.example.hura.domain.model.TransactionType
import com.example.hura.notification.model.RawNotification

class NLBNotificationParser : NotificationParser {
    override fun canHandle(packageName: String): Boolean{
        return packageName == "hr.asseco.android.jimba.tutunskamk.production"
    }

    override fun parse(notification: RawNotification): ParsedTransaction? {
        val text = notification.text ?: return null
        val type = when {
            text.contains("USPESNO plakjanje", ignoreCase = true) -> TransactionType.EXPENSE
            text.contains("PRILIV", ignoreCase = true) -> TransactionType.INCOME
            else -> return null
        }
        //Extracts the amount by looking for a .2f number
        val amountRegex = Regex("""\d+\.\d{2}""")
        val amountMatch = amountRegex.find(text) ?: return null
        val rawAmount = amountMatch.value.toBigDecimalOrNull() ?: return null

        //Extracts currency by looking for the first token after the amount
        val afterAmount = text.substring(amountMatch.range.last + 1).trim()
        val currency = afterAmount.split(" ").firstOrNull() ?: return null

        //Extracts merchant by checking for everything between prodazno mesto and the first dot
        val merchant = text
            .substringAfter("na prodazno mesto", "")
            .substringBefore(".")
            .trim()
            .ifEmpty { return null }

        return ParsedTransaction(
            amount = rawAmount,
            currency = currency,
            bankName = "NLB",
            timestamp = notification.timestamp,
            type = type,
            merchant = merchant,
            sourcePackage = notification.packageName,
            notificationKey = notification.notificationKey
        )
    }
}