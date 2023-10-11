package hardcoder.dev.logics.features.diary

import hardcoder.dev.entities.features.diary.DateRangeFilterType
import kotlinx.coroutines.flow.flowOf

class DateRangeFilterTypeProvider {

    fun provideAllDateRangeFilters() = flowOf(DateRangeFilterType.entries)
}