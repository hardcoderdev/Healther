package hardcoder.dev.logic.features.diary

import hardcoder.dev.datetime.getEndOfDay
import hardcoder.dev.datetime.getStartOfDay
import hardcoder.dev.datetime.currentDate
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

class DateRangeFilterTypeMapper(appPreferenceProvider: AppPreferenceProvider) {
    private val mapperScope = CoroutineScope(Dispatchers.IO + Job())
    private val appPreferences = appPreferenceProvider.provideAppPreference().stateIn(
        scope = mapperScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    ).value

    // TODO FIRST LINE
    private val map = mapOf(
        DateRangeFilterType.BY_ALL_PERIOD to Clock.System.now()
                ..LocalDate.currentDate().getEndOfDay(),
        DateRangeFilterType.BY_DAY to LocalDate.currentDate().getStartOfDay()
                ..LocalDate.currentDate().getEndOfDay(),
        DateRangeFilterType.BY_WEEK to LocalDate.currentDate().minus(7, DateTimeUnit.DAY).getStartOfDay()
                ..LocalDate.currentDate().getEndOfDay(),
        DateRangeFilterType.BY_MONTH to LocalDate.currentDate().minus(1, DateTimeUnit.MONTH).getStartOfDay()
                ..LocalDate.currentDate().getEndOfDay(),
        DateRangeFilterType.BY_YEAR to LocalDate.currentDate().minus(1, DateTimeUnit.YEAR).getStartOfDay()
                ..LocalDate.currentDate().getEndOfDay()
    )

    fun map(dateRangeFilterType: DateRangeFilterType) = checkNotNull(map[dateRangeFilterType])
}