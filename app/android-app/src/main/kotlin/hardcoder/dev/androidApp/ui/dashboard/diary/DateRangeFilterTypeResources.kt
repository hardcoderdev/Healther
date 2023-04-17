package hardcoder.dev.androidApp.ui.dashboard.diary

import androidx.annotation.StringRes
import hardcoder.dev.logic.dashboard.features.DateRangeFilterType

data class DateRangeFilterTypeResources(
    @StringRes val nameResId: Int,
    val dateRangeFilterType: DateRangeFilterType
)