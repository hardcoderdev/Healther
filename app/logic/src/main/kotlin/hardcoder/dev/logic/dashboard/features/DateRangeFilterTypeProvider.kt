package hardcoder.dev.logic.dashboard.features

import kotlinx.coroutines.flow.flowOf

class DateRangeFilterTypeProvider {

    fun provideAllDateRangeFilters() = flowOf(DateRangeFilterType.values().toList())
}