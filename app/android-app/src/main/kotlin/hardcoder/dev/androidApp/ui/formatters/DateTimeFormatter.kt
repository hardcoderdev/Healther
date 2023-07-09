package hardcoder.dev.androidApp.ui.formatters

import android.content.Context
import android.text.format.DateFormat

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toLocalDateTime

class DateTimeFormatter(context: Context) {

    private val appTimeZone get() = TimeZone.currentSystemDefault()
    private val dateFormatter = DateTimeFormatter.ofLocalizedDate(dateFormatStyle)
    private val timeFormatter = DateTimeFormatter.ofPattern(
        if (DateFormat.is24HourFormat(context)) {
            "HH:mm"
        } else {
            "hh:mm a"
        },
    )

    fun formatTime(
        instant: Instant,
    ): String {
        val javaDateTime = instant.toLocalDateTime(appTimeZone).toJavaLocalDateTime()
        return timeFormatter.format(javaDateTime)
    }

    fun formatTime(
        localTime: LocalTime,
    ): String {
        val javaLocalTime = localTime.toJavaLocalTime()
        return timeFormatter.format(javaLocalTime)
    }

    fun formatDate(localDate: LocalDate): String {
        val javaLocalDate = localDate.toJavaLocalDate()
        return dateFormatter.format(javaLocalDate)
    }

    companion object {
        private val dateFormatStyle = FormatStyle.LONG
    }
}