package hardcoder.dev.logic.dashboard.features

import hardcoder.dev.extensions.getEndOfDay
import hardcoder.dev.extensions.getStartOfDay
import hardcoder.dev.logic.appPreferences.AppPreference
import hardcoder.dev.logic.appPreferences.AppPreferenceProvider
import io.github.boguszpawlowski.composecalendar.kotlinxDateTime.now
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

class DateRangeFilterTypeMapper(appPreferenceProvider: AppPreferenceProvider) {
    private val mapperScope = CoroutineScope(Dispatchers.IO + Job())
    private val appPreferences = appPreferenceProvider.provideAppPreference().stateIn(
        scope = mapperScope,
        started = SharingStarted.Eagerly,
        initialValue = AppPreference(Instant.fromEpochSeconds(0))
    ).value

    private val map = mapOf(
        DateRangeFilterType.ByAllPeriod to requireNotNull(appPreferences).firstLaunchTime
                ..LocalDate.now().getEndOfDay(),
        DateRangeFilterType.ByDay to LocalDate.now().getStartOfDay()
                ..LocalDate.now().getEndOfDay(),
        DateRangeFilterType.ByWeek to LocalDate.now().minus(7, DateTimeUnit.DAY).getStartOfDay()
                ..LocalDate.now().getEndOfDay(),
        DateRangeFilterType.ByMonth to LocalDate.now().minus(1, DateTimeUnit.MONTH).getStartOfDay()
                ..LocalDate.now().getEndOfDay(),
        DateRangeFilterType.ByYear to LocalDate.now().minus(1, DateTimeUnit.YEAR).getStartOfDay()
                ..LocalDate.now().getEndOfDay()
    )

    fun map(dateRangeFilterType: DateRangeFilterType) = checkNotNull(map[dateRangeFilterType])
}