package hardcoder.dev.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun LocalDate.getStartOfDay(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): Instant {
    return atTime(LocalTime(hour = 0, minute = 0, second = 59)).toInstant(timeZone)
}

fun LocalDate.toInstant(localTime: LocalTime) = LocalDateTime(
    date = this,
    time = localTime,
).toInstant(TimeZone.currentSystemDefault())

fun LocalDate.getEndOfDay(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): Instant {
    return atTime(LocalTime(hour = 23, minute = 59, second = 59)).toInstant(timeZone)
}

fun Long.millisToLocalDateTime(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
) = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)

fun Instant.toLocalDateTime(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
) = toLocalDateTime(timeZone)