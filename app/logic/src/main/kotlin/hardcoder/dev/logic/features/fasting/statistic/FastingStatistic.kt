package hardcoder.dev.logic.features.fasting.statistic

import hardcoder.dev.logic.features.fasting.plan.FastingPlan

data class FastingStatistic(
    val duration: FastingDurationStatistic?,
    val favouritePlan: FastingPlan?,
)