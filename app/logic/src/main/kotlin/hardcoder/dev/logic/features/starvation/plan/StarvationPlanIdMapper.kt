package hardcoder.dev.logic.features.starvation.plan

import hardcoder.dev.entities.features.starvation.StarvationPlan

class StarvationPlanIdMapper {

    private val map = mapOf(
        StarvationPlan.PLAN_14_10 to 0,
        StarvationPlan.PLAN_16_8 to 1,
        StarvationPlan.PLAN_18_6 to 2,
        StarvationPlan.PLAN_20_4 to 3,
        StarvationPlan.CUSTOM_PLAN to 4
    )

    fun mapToId(starvationPlan: StarvationPlan) = checkNotNull(map[starvationPlan])

    fun mapToStarvationPlan(planId: Int) = checkNotNull(
        map.entries.find { it.value == planId }
    ).key
}