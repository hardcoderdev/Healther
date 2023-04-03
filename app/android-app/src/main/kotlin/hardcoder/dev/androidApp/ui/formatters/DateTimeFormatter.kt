package hardcoder.dev.androidApp.ui.formatters

import android.content.Context
import android.text.format.DateFormat
import hardcoder.dev.extensions.toDate
import hardcoder.dev.healther.R
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import java.text.SimpleDateFormat
import java.util.Locale

class DateTimeFormatter(
    private val context: Context,
    private val defaultAccuracy: Accuracy
) {

    private val dateTimeFormatPattern = buildString {
        append("HH:mm")
        if (!DateFormat.is24HourFormat(context)) append(" a")
    }

    private val timeFormatPattern = buildString {
        append("dd.MM, HH:mm:ss")
        if (!DateFormat.is24HourFormat(context)) append(" a")
    }

    fun formatDateTime(date: LocalDate): String {
        val simpleDateFormat = SimpleDateFormat(dateTimeFormatPattern, Locale.getDefault())
        return simpleDateFormat.format(date.toDate(TimeZone.currentSystemDefault()))
    }

    fun formatTime(time: Instant): String {
        val simpleDateFormat = SimpleDateFormat(timeFormatPattern, Locale.getDefault())
        return simpleDateFormat.format(time.toEpochMilliseconds())
    }

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
}