package hardcoder.dev.androidApp.ui.formatters

import android.content.Context
import android.text.format.DateFormat
import hardcoder.dev.healther.R
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class DateTimeFormatter(
    private val context: Context,
    private val defaultAccuracy: Accuracy
) {

    private val appTimeZone get() = TimeZone.currentSystemDefault()
    private val dateFormatter = DateTimeFormatter.ofLocalizedDate(dateFormatStyle)
    private val timeFormatter = DateTimeFormatter.ofPattern(
        if (DateFormat.is24HourFormat(context)) "HH:mm"
        else "hh:mm a"
    )

    fun formatDate(
        instant: Instant,
    ) = formatDate(instant.toLocalDateTime(appTimeZone).date)

    fun formatTime(
        instant: Instant
    ): String {
        val javaDateTime = instant.toLocalDateTime(appTimeZone).toJavaLocalDateTime()
        return timeFormatter.format(javaDateTime)
    }

    fun formatDateTime(
        instant: Instant,
    ) = formatDateTime(instant.toLocalDateTime(appTimeZone))

    private fun formatDate(
        date: LocalDate,
    ) = dateFormatter.format(date.toJavaLocalDate())!!

    private fun formatDateTime(
        dateTime: LocalDateTime
    ): String {
        val jDateTime = dateTime.toJavaLocalDateTime()
        return dateFormatter.format(
            jDateTime.toLocalDate()
        ) + ", " + timeFormatter.format(
            jDateTime.toLocalTime()
        )
    }

    private fun formatTimeZone(
        timeZone: TimeZone
    ) = android.icu.util.TimeZone.getTimeZone(timeZone.id).getDisplayName(
        false,
        timeZoneFormatStyle
    )!!

    fun formatMillisDistance(
        distanceInMillis: Long,
        accuracy: Accuracy = defaultAccuracy,
        usePlurals: Boolean = false
    ) = buildString {
        val seconds = distanceInMillis / 1000 % 60
        val minutes = distanceInMillis / 1000 / 60 % 60
        val hours = distanceInMillis / 1000 / 60 / 60 % 24
        val days = distanceInMillis / 1000 / 60 / 60 / 24

        val appendDays = days != 0L
        val appendHours = hours != 0L && (!appendDays || accuracy.order > 1)
        val appendMinutes = minutes != 0L && (!appendDays && !appendHours || accuracy.order > 2)
        val appendSeconds = !appendDays && !appendHours && !appendMinutes || accuracy.order > 3

        val daysPlurals = context.resources.getQuantityString(R.plurals.days, days.toInt())
        val hoursPlurals = context.resources.getQuantityString(R.plurals.hours, hours.toInt())
        val minutesPlurals = context.resources.getQuantityString(R.plurals.minutes, minutes.toInt())
        val secondsPlurals = context.resources.getQuantityString(R.plurals.seconds, seconds.toInt())

        if (appendDays) {
            if (usePlurals) {
                append(days)
                append(" ")
                append(daysPlurals)
            } else {
                append(context.getString(R.string.datetime_days, days))
            }
        }

        if (appendHours) {
            if (appendDays) append(" ")
            if (usePlurals) {
                append(hours)
                append(" ")
                append(hoursPlurals)
            } else {
                append(context.getString(R.string.datetime_hours, hours))
            }
        }

        if (appendMinutes) {
            if (appendHours || appendDays) append(" ")
            if (usePlurals) {
                append(minutes)
                append(" ")
                append(minutesPlurals)
            } else {
                append(context.getString(R.string.datetime_minutes, minutes))
            }
        }

        if (appendSeconds) {
            if (appendMinutes || appendHours || appendDays) append(" ")
            if (usePlurals) {
                append(seconds)
                append(" ")
                append(secondsPlurals)
            } else {
                append(context.getString(R.string.datetime_seconds, seconds))
            }
        }
    }

    enum class Accuracy(val order: Int) {
        DAYS(1),
        HOURS(2),
        MINUTES(3),
        SECONDS(4)
    }

    companion object {
        private val dateFormatStyle = FormatStyle.LONG
        private const val timeZoneFormatStyle = android.icu.util.TimeZone.LONG_GMT
    }
}