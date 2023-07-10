package hardcoder.dev.logic.features.diary

import hardcoder.dev.datetime.DateTimeProvider
import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.minus

class DateRangeFilterTypeMapper(
    appPreferenceProvider: AppPreferenceProvider,
    private val dateTimeProvider: DateTimeProvider,
) {

    private val mapperScope = CoroutineScope(Dispatchers.IO + Job())
    private val firstLaunchTime = appPreferenceProvider.provideAppPreference().map {
        requireNotNull(it).firstLaunchTime
    }.stateIn(
        scope = mapperScope,
        started = SharingStarted.Eagerly,
        initialValue = dateTimeProvider.currentInstant(),
    )

    suspend fun map(dateRangeFilterType: DateRangeFilterType): ClosedRange<Instant> {
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
                firstLaunchTime.first()..dateTimeProvider.currentInstant()
            }
        }
    }
}