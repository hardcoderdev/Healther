package hardcoder.dev.androidApp.ui.features.starvation

import hardcoder.dev.healther.R

class StarvationStatisticLabelResolver {

    private val map = mapOf(
        R.string.starvation_statistic_max_hours_label to 0,
        R.string.starvation_statistic_min_hours_label to 1,
        R.string.starvation_statistic_average_hours_label to 2,
        R.string.starvation_statistic_completion_percentage_label to 3
    )

    fun resolve(labelId: Int) = checkNotNull(
        map.entries.find { it.value == labelId }
    ).key
}