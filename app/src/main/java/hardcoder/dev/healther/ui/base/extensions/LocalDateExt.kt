package hardcoder.dev.healther.ui.base.extensions

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaInstant
import java.util.Date

fun LocalDate.getStartOfDay(): Long {
    return atTime(LocalTime(hour = 0, minute = 0, second = 59)).toInstant(TimeZone.UTC).epochSeconds
}

fun LocalDate.getEndOfDay(): Long {
    return atTime(
        LocalTime(
            hour = 23,
            minute = 59,
            second = 59
        )
    ).toInstant(TimeZone.UTC).epochSeconds
}

fun LocalDate.createRangeForCurrentDay(): LongRange {
    return getStartOfDay()..getEndOfDay()
}

fun LocalDate.toDate(): Date {
    return Date.from(
        atTime(LocalTime(hour = 0, minute = 0, second = 59))
            .toInstant(TimeZone.UTC)
            .toJavaInstant()
    )
}