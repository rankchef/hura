package com.example.hura.ui.category

import com.example.hura.R
object CategoryIcons {
    val all: Map<String, Int> = mapOf(
        "fastfood" to R.drawable.ic_fastfood,
        "flight" to R.drawable.ic_flight,
        "gas_station" to R.drawable.ic_gas_station,
        "groceries" to R.drawable.ic_groceries,
        "home" to R.drawable.ic_home,
        "lightbulb" to R.drawable.ic_lightbulb,
        "phone" to R.drawable.ic_phone,
        "pill" to R.drawable.ic_pill,
        "receipt_long" to R.drawable.ic_receipt_long,
        "shopping_bag" to R.drawable.ic_shopping_bag,
        "subscription" to R.drawable.ic_subscription,
        "transport" to R.drawable.ic_transport
    )

    val fastfood get() = all["fastfood"]!!
    val flight get() = all["flight"]!!
    val gasStation get() = all["gas_station"]!!
    val groceries get() = all["groceries"]!!
    val home get() = all["home"]!!
    val lightbulb get() = all["lightbulb"]!!
    val phone get() = all["phone"]!!
    val pill get() = all["pill"]!!
    val receiptLong get() = all["receipt_long"]!!
    val shoppingBag get() = all["shopping_bag"]!!
    val subscription get() = all["subscription"]!!
    val transport get() = all["transport"]!!
}