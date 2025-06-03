package com.monoexpenses.utils

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import com.kizitonwose.calendar.core.YearMonth
import com.kizitonwose.calendar.core.minus
import com.kizitonwose.calendar.core.plus
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
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

val YearMonth.next: YearMonth get() = this.plus(1, DateTimeUnit.MONTH)
val YearMonth.previous: YearMonth get() = this.minus(1, DateTimeUnit.MONTH)

fun YearMonth.displayText(): String {
    return "${month.displayText()} $year"
}

fun Month.displayText(): String {
    return getDisplayName()
}

fun Month.getDisplayName(): String {
    return MonthNames.ENGLISH_FULL.names[this.number -1]
}

fun DayOfWeek.getDisplayName(): String {
    return DayOfWeekNames.ENGLISH_ABBREVIATED.names[isoDayNumber - 1]
}

private val enLocale = Locale("en-US")

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName().let { value ->
        if (uppercase) value.toUpperCase(enLocale) else value
    }
}

fun LocalDate.toEpochSeconds(
    timeZone: TimeZone = TimeZone.UTC,
    localTime: LocalTime,
): Long {
    val localDateTime = LocalDateTime(this, localTime)
    val instant = localDateTime.toInstant(timeZone)
    return instant.epochSeconds
}
