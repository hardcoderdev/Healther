package hardcoder.dev.logic.features.starvation.plan

import hardcoder.dev.datetime.TimeUnitMapper
import hardcoder.dev.entities.features.starvation.StarvationPlan

class StarvationPlanDurationResolver(private val timeUnitMapper: TimeUnitMapper) {

    fun resolve(starvationPlan: StarvationPlan, customStarvingHoursCount: Int?): Long {
        return when (starvationPlan) {
            StarvationPlan.PLAN_14_10 -> {
                timeUnitMapper.hoursToMillis(14)
            }
            StarvationPlan.PLAN_16_8 -> {
                timeUnitMapper.hoursToMillis(16)
            }
            StarvationPlan.PLAN_18_6 -> {
                timeUnitMapper.hoursToMillis(18)
            }
            StarvationPlan.PLAN_20_4 -> {
                timeUnitMapper.hoursToMillis(20)
            }
            StarvationPlan.CUSTOM_PLAN -> {
                timeUnitMapper.hoursToMillis(requireNotNull(customStarvingHoursCount).toLong())
            }
        }
    }
}
