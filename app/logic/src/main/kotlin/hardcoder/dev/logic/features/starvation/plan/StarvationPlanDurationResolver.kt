package hardcoder.dev.logic.features.starvation.plan

import hardcoder.dev.entities.features.starvation.StarvationPlan

class StarvationPlanDurationResolver {

    private val map = mapOf(
        StarvationPlan.PLAN_14_10 to 14 * HOUR_TO_MILLIS_FORMULA,
        StarvationPlan.PLAN_16_8 to 16 * HOUR_TO_MILLIS_FORMULA,
        StarvationPlan.PLAN_18_6 to 18 * HOUR_TO_MILLIS_FORMULA,
        StarvationPlan.PLAN_20_4 to 20 * HOUR_TO_MILLIS_FORMULA,
        StarvationPlan.CUSTOM_PLAN to Int.MAX_VALUE * HOUR_TO_MILLIS_FORMULA
    )

    fun resolve(starvationPlan: StarvationPlan) = checkNotNull(map[starvationPlan])

    private companion object {
        private const val HOUR_TO_MILLIS_FORMULA = 60 * 60 * 1000L
    }
}
