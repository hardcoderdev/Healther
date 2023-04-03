package hardcoder.dev.logic.features.fasting.plan

import hardcoder.dev.logic.entities.features.fasting.FastingPlan

class FastingPlanIdMapper {

    private val map = mapOf(
        FastingPlan.PLAN_14_10 to 0,
        FastingPlan.PLAN_16_8 to 1,
        FastingPlan.PLAN_18_6 to 2,
        FastingPlan.PLAN_20_4 to 3,
        FastingPlan.CUSTOM_PLAN to 4
    )

    fun mapToId(fastingPlan: FastingPlan) = checkNotNull(map[fastingPlan])

    fun mapToFastingPlan(planId: Int) = checkNotNull(
        map.entries.find { it.value == planId }
    ).key
}