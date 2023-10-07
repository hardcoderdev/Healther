package hardcoder.dev.resources.features.fasting

import hardcoder.dev.entities.features.fasting.FastingPlan

data class FastingPlanResources(
    val nameResId: Int,
    val fastingHoursCount: Int,
    val eatingHoursCount: Int,
    val fastingPlan: FastingPlan,
)