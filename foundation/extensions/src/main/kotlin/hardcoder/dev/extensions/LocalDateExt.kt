package hardcoder.dev.extensions

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

fun LocalDate.createRangeForCurrentDay(timeZone: TimeZone = TimeZone.currentSystemDefault()): ClosedRange<Instant> {
    return getStartOfDay(timeZone)..getEndOfDay(timeZone)
}

fun LocalDate.toDate(timeZone: TimeZone = TimeZone.UTC): Date {
    return Date.from(
        atTime(LocalTime(hour = 0, minute = 0, second = 59))
            .toInstant(timeZone)
            .toJavaInstant()
    )
}

fun LocalDateTime.toMillis(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = toInstant(timeZone).toEpochMilliseconds()

fun Long.millisToLocalDateTime(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)

fun millisDistanceBetween(
    range: ClosedRange<LocalDateTime>,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = range.endInclusive.toMillis(timeZone) - range.start.toMillis(timeZone)