package hardcoder.dev.entities.features.fasting.statistic

import hardcoder.dev.entities.features.fasting.FastingPlan

data class FastingStatistic(
    val fastingDurationStatistic: FastingDurationStatistic?,
    val percentageCompleted: Int?,
    val favouritePlan: FastingPlan?
)

