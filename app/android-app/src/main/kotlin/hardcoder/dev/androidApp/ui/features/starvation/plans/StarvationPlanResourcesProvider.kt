package hardcoder.dev.androidApp.ui.features.starvation.plans

import hardcoder.dev.entities.features.starvation.StarvationPlan
import hardcoder.dev.healther.R

class StarvationPlanResourcesProvider {

    private val map = mapOf(
        StarvationPlan.PLAN_14_10 to listOf(
            R.string.starvationPlanItem_14_10,
            14,
            10
        ),
        StarvationPlan.PLAN_16_8 to listOf(
            R.string.starvationPlanItem_16_8,
            16,
            8
        ),
        StarvationPlan.PLAN_18_6 to listOf(
            R.string.starvationPlanItem_18_6,
            18,
            6
        ),
        StarvationPlan.PLAN_20_4 to listOf(
            R.string.starvationPlanItem_20_4,
            20,
            4
        ),
        StarvationPlan.CUSTOM_PLAN to listOf(
            R.string.starvationPlanItem_customPlan,
            Int.MAX_VALUE,
            0
        )
    )

    fun provide(starvationPlan: StarvationPlan) = checkNotNull(map[starvationPlan]).let {
        StarvationPlanResources(
            nameResId = it[0],
            starvingHoursCount = it[1],
            eatingHoursCount = it[2],
            starvationPlan = starvationPlan
        )
    }
}