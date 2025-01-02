package org.umcs.mobile.data

import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime

fun convertMillisToDate(millis: Long): String {
    val date = Instant.fromEpochMilliseconds(millis)
    val localDate = date.toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
    return "${localDate.year}/${
        localDate.monthNumber.toString().padStart(2, '0')
    }/${localDate.dayOfMonth.toString().padStart(2, '0')}"
}