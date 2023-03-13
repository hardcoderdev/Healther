package hardcoder.dev.androidApp.ui.features.starvation.plans

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import hardcoder.dev.entities.features.starvation.StarvationPlan

data class StarvationPlanResources(
    @StringRes val nameResId: Int,
    @DrawableRes val imageResId: Int,
    val starvingHoursCount: Int,
    val eatingHoursCount: Int,
    val starvationPlan: StarvationPlan
)