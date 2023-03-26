package hardcoder.dev.androidApp.ui.features.fasting.plans

import hardcoder.dev.entities.features.fasting.FastingPlan
import hardcoder.dev.healther.R

class FastingPlanResourcesProvider {

    private val map = mapOf(
        FastingPlan.PLAN_14_10 to listOf(
            R.string.fastingPlanItem_14_10,
            14,
            10
        ),
        FastingPlan.PLAN_16_8 to listOf(
            R.string.fastingPlanItem_16_8,
            16,
            8
        ),
        FastingPlan.PLAN_18_6 to listOf(
            R.string.fastingPlanItem_18_6,
            18,
            6
        ),
        FastingPlan.PLAN_20_4 to listOf(
            R.string.fastingPlanItem_20_4,
            20,
            4
        ),
        FastingPlan.CUSTOM_PLAN to listOf(
            R.string.fastingPlanItem_customPlan,
            Int.MAX_VALUE,
            0
        )
    )

    fun provide(fastingPlan: FastingPlan) = checkNotNull(map[fastingPlan]).let {
        FastingPlanResources(
            nameResId = it[0],
            fastingHoursCount = it[1],
            eatingHoursCount = it[2],
            fastingPlan = fastingPlan
        )
    }
}