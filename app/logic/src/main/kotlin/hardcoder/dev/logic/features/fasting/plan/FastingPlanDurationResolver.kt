package hardcoder.dev.logic.features.fasting.plan

import kotlin.time.Duration.Companion.hours

class FastingPlanDurationResolver {

    fun resolve(fastingPlan: FastingPlan, customStarvingHoursCount: Int?): Long {
        return when (fastingPlan) {
            FastingPlan.PLAN_14_10 -> 14.hours.inWholeHours
            FastingPlan.PLAN_16_8 -> 16.hours.inWholeHours
            FastingPlan.PLAN_18_6 -> 18.hours.inWholeHours
            FastingPlan.PLAN_20_4 -> 20.hours.inWholeHours
            FastingPlan.CUSTOM_PLAN -> requireNotNull(customStarvingHoursCount).hours.inWholeHours
        }
    }
}