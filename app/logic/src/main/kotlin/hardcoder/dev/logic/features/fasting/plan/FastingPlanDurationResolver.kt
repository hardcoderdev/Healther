package hardcoder.dev.logic.features.fasting.plan

import hardcoder.dev.datetime.TimeUnitMapper
import hardcoder.dev.entities.features.fasting.FastingPlan

class FastingPlanDurationResolver(private val timeUnitMapper: TimeUnitMapper) {

    fun resolve(fastingPlan: FastingPlan, customStarvingHoursCount: Int?): Long {
        return when (fastingPlan) {
            FastingPlan.PLAN_14_10 -> {
                timeUnitMapper.hoursToMillis(14)
            }
            FastingPlan.PLAN_16_8 -> {
                timeUnitMapper.hoursToMillis(16)
            }
            FastingPlan.PLAN_18_6 -> {
                timeUnitMapper.hoursToMillis(18)
            }
            FastingPlan.PLAN_20_4 -> {
                timeUnitMapper.hoursToMillis(20)
            }
            FastingPlan.CUSTOM_PLAN -> {
                timeUnitMapper.hoursToMillis(requireNotNull(customStarvingHoursCount).toLong())
            }
        }
    }
}
