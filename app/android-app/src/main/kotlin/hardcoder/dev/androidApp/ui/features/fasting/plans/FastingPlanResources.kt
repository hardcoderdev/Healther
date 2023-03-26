package hardcoder.dev.androidApp.ui.features.fasting.plans

import androidx.annotation.StringRes
import hardcoder.dev.entities.features.fasting.FastingPlan

data class FastingPlanResources(
    @StringRes val nameResId: Int,
    val fastingHoursCount: Int,
    val eatingHoursCount: Int,
    val fastingPlan: FastingPlan
)