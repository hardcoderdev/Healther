package hardcoder.dev.mappers.features.diary

import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.entities.features.diary.DateRangeFilterType
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.minus

class DateRangeFilterTypeMapper(private val dateTimeProvider: DateTimeProvider) {

    fun map(
        firstLaunchTime: Instant,
        dateRangeFilterType: DateRangeFilterType,
    ): ClosedRange<Instant> {
        val currentDate = dateTimeProvider.currentDate()

        return when (dateRangeFilterType) {
            DateRangeFilterType.BY_DAY -> {
                dateTimeProvider.currentDateRange()
            }

            DateRangeFilterType.BY_WEEK -> {
                currentDate.minus(1, DateTimeUnit.WEEK).getStartOfDay()..currentDate.getEndOfDay()
            }

            DateRangeFilterType.BY_MONTH -> {
                currentDate.minus(1, DateTimeUnit.MONTH).getStartOfDay()..currentDate.getEndOfDay()
            }

            DateRangeFilterType.BY_YEAR -> {
                currentDate.minus(1, DateTimeUnit.YEAR).getStartOfDay()..currentDate.getEndOfDay()
            }

            DateRangeFilterType.BY_ALL_PERIOD -> {
                firstLaunchTime..dateTimeProvider.currentInstant()
            }
        }
    }
}