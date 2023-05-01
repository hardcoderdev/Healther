package hardcoder.dev.androidApp.ui.dashboard.diary

import hardcoder.dev.healther.R
import hardcoder.dev.logic.dashboard.features.DateRangeFilterType

class DateRangeFilterTypeResourcesProvider {

    private val map = mapOf(
        DateRangeFilterType.BY_DAY to R.string.dateRangeFilterType_byDay,
        DateRangeFilterType.BY_WEEK to R.string.dateRangeFilterType_byWeek,
        DateRangeFilterType.BY_MONTH to R.string.dateRangeFilterType_byMonth,
        DateRangeFilterType.BY_YEAR to R.string.dateRangeFilterType_byYear,
        DateRangeFilterType.BY_ALL_PERIOD to R.string.dateRangeFilterType_byAllPeriod,
    )

    fun provide(dateRangeFilterType: DateRangeFilterType) = checkNotNull(map[dateRangeFilterType]).let {
        DateRangeFilterTypeResources(
            nameResId = it,
            dateRangeFilterType = dateRangeFilterType
        )
    }
}