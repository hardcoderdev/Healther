package hardcoder.dev.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime
import java.util.Date

fun LocalDate.Companion.currentDate(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): LocalDate {
    return Clock.System.now().toLocalDateTime(timeZone).date
}

fun LocalDate.getStartOfDay(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): Instant {
    return atTime(LocalTime(hour = 0, minute = 0, second = 59)).toInstant(timeZone)
}

fun LocalDate.getEndOfDay(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
): Instant {
    return atTime(LocalTime(hour = 23, minute = 59, second = 59)).toInstant(timeZone)
}

fun LocalDate.Companion.createRangeForCurrentDay(timeZone: TimeZone = TimeZone.currentSystemDefault()): ClosedRange<Instant> {
    return currentDate().getStartOfDay(timeZone)..currentDate().getEndOfDay(timeZone)
}

fun Long.millisToLocalDateTime(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)