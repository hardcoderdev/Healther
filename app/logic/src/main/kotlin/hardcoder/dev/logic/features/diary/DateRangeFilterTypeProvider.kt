package hardcoder.dev.logic.features.diary

import kotlinx.coroutines.flow.flowOf

class DateRangeFilterTypeProvider {

    fun provideAllDateRangeFilters() = flowOf(DateRangeFilterType.entries)
}