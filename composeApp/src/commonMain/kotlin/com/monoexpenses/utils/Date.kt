package com.monoexpenses.utils

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

fun formatEpochSecondsToDayMonth(seconds: Long): String {
    val instant = Instant.fromEpochSeconds(seconds)

    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

    val format = LocalDate.Format {
        dayOfMonth(Padding.ZERO)
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
    }

    return format.format(date)
}

fun formatEpochSecondsToTimeDayMonthWeek(seconds: Long): String {
    val instant = Instant.fromEpochSeconds(seconds)

    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val format = LocalDateTime.Format {
        hour()
        char(':')
        minute(Padding.ZERO)
        this.chars(", ")
        dayOfMonth(Padding.ZERO)
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        this.chars(", ")
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
    }

    return format.format(dateTime)
}
