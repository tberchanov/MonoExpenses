package com.monoexpenses.utils

import kotlin.math.absoluteValue

fun formatMoney(cents: Long): String {
    val dollars = cents / 100.0
    return if (cents % 100 == 0L) {
        dollars.toInt().toString()
    } else {
        val whole = dollars.toInt()
        val decimal = ((dollars - whole) * 100).toInt().absoluteValue
        "$whole.${decimal.toString().padStart(2, '0')}"
    }
}
