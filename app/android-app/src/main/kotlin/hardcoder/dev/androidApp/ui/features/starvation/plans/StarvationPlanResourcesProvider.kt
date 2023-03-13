package hardcoder.dev.androidApp.ui.features.starvation.plans

import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.healther.R

class StarvationPlanResourcesProvider {

    private val map = mapOf(
        StarvationPlan.PLAN_14_10 to listOf(
            R.string.starvationPlan_14_10,
            R.drawable.starvation_plans_plan_14_10,
            14,
            10
        ),
        StarvationPlan.PLAN_16_8 to listOf(
            R.string.starvationPlan_16_8,
            R.drawable.starvation_plans_plan_16_8,
            16,
            8
        ),
        StarvationPlan.PLAN_18_6 to listOf(
            R.string.starvationPlan_18_6,
            R.drawable.starvation_plans_plan_18_6,
            18,
            6
        ),
        StarvationPlan.PLAN_20_4 to listOf(
            R.string.starvationPlan_20_4,
            R.drawable.starvation_plans_plan_20_4,
            20,
            4
        ),
        StarvationPlan.CUSTOM_PLAN to listOf(
            R.string.starvation_plans_plan_custom,
            R.drawable.starvation_plans_plan_custom,
            Int.MAX_VALUE,
            0
        )
    )

    fun provide(starvationPlan: StarvationPlan) = checkNotNull(map[starvationPlan]).let {
        StarvationPlanResources(
            nameResId = it[0],
            imageResId = it[1],
            starvingHoursCount = it[2],
            eatingHoursCount = it[3],
            starvationPlan = starvationPlan
        )
    }
}