package hardcoder.dev.androidApp.ui

import android.content.Context
import hardcoder.dev.healther.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateTimeFormatter(
    private val context: Context,
    private val defaultAccuracy: Accuracy
) {

    fun formatDateTime(
        date: Date,
        formatPattern: String = DEFAULT_PATTERN
    ): String {
        val simpleDateFormat = SimpleDateFormat(formatPattern, Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun formatDateTime(
        dateInMillis: Long,
        formatPattern: String = DEFAULT_PATTERN
    ): String {
        val date = Date(dateInMillis)
        val simpleDateFormat = SimpleDateFormat(formatPattern, Locale.getDefault())
        return simpleDateFormat.format(date)
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

    private companion object {
        private const val DEFAULT_PATTERN = "dd.MM, HH:mm:ss"
    }
}