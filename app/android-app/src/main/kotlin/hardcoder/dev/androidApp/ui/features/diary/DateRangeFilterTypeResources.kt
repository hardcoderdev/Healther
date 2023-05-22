package hardcoder.dev.androidApp.ui.features.diary

import androidx.annotation.StringRes
import hardcoder.dev.logic.features.diary.DateRangeFilterType

data class DateRangeFilterTypeResources(
    @StringRes val nameResId: Int,
    val dateRangeFilterType: DateRangeFilterType
)