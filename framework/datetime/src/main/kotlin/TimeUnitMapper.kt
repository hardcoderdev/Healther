package hardcoder.dev.datetime

class TimeUnitMapper {

    fun hoursToMillis(hoursInMillis: Long) = hoursInMillis * HOURS_TO_MILLIS_FORMULA

    fun millisToHours(hours: Long) = hours / 1000L / 60 / 60

    fun minutesToMillis(minutesInMillis: Long) = minutesInMillis * MINUTES_TO_MILLIS_FORMULA

    fun millisToMinutes(minutes: Long) = minutes / 1000L / 60

    private companion object {
        private const val HOURS_TO_MILLIS_FORMULA = 60 * 60 * 1000L
        private const val MINUTES_TO_MILLIS_FORMULA = 60 * 1000L
    }
}