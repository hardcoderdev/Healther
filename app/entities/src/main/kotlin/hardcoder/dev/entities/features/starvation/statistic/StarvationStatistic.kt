package hardcoder.dev.entities.features.starvation.statistic

import hardcoder.dev.entities.features.starvation.StarvationPlan

data class StarvationStatistic(
    val starvationDurationStatistic: StarvationDurationStatistic?,
    val percentageCompleted: Int?,
    val favouritePlan: StarvationPlan?
)

