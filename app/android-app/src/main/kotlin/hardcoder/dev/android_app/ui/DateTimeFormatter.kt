package hardcoder.dev.android_app.ui

import android.content.Context
import hardcoder.dev.healther.R

class DateTimeFormatter(
    private val context: Context,
    private val defaultAccuracy: Accuracy
) {

    fun formatMillisDistance(
        distanceInMillis: Long,
        accuracy: Accuracy = defaultAccuracy
    ) = buildString {
        val seconds = distanceInMillis / 1000 % 60
        val minutes = distanceInMillis / 1000 / 60 % 60
        val hours = distanceInMillis / 1000 / 60 / 60 % 24
        val days = distanceInMillis / 1000 / 60 / 60 / 24

        val appendDays = days != 0L
        val appendHours = hours != 0L && (!appendDays || accuracy.order > 1)
        val appendMinutes = minutes != 0L && (!appendDays && !appendHours || accuracy.order > 2)
        val appendSeconds = !appendDays && !appendHours && !appendMinutes || accuracy.order > 3

        if (appendDays) {
            append(context.getString(R.string.datetime_days, days))
        }

        if (appendHours) {
            if (appendDays) append(" ")
            append(context.getString(R.string.datetime_hours, hours))
        }

        if (appendMinutes) {
            if (appendHours || appendDays) append(" ")
            append(context.getString(R.string.datetime_minutes, minutes))
        }

        if (appendSeconds) {
            if (appendMinutes || appendHours || appendDays) append(" ")
            append(context.getString(R.string.datetime_seconds, seconds))
        }
    }

    enum class Accuracy(val order: Int) {
        DAYS(4),
        HOURS(3),
        MINUTES(1),
        SECONDS(0)
    }
}